package com.example.pastebox.service;

import com.example.pastebox.dto.PasteboxRequestDto;
import com.example.pastebox.dto.PasteboxResponseDto;
import com.example.pastebox.entity.Pastebox;
import com.example.pastebox.entity.PasteboxStatus;
import com.example.pastebox.repository.PasteboxRepository;
import com.example.pastebox.util.error.exception.ExpiredPasteboxException;
import com.example.pastebox.util.error.exception.NoSuchPasteboxException;
import com.example.pastebox.util.mapper.EntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PasteboxServiceImpl implements PasteboxService{

    private final PasteboxRepository pasteboxRepository;
    private final EntityMapper entityMapper;

    @Transactional
    @Override
    public PasteboxResponseDto createPastebox(PasteboxRequestDto dto) {
        Pastebox pastebox = entityMapper.mapPasteboxDtoToPastebox(dto);
        pastebox = pasteboxRepository.save(pastebox);
        pastebox.setHash(Integer.toHexString(pastebox.hashCode())+pastebox.getId());
        return entityMapper.mapPasteboxToPasteboxResponseDto(pasteboxRepository.save(pastebox));
    }


    @Override
    public PasteboxResponseDto getForUrl(String url) {
        Pastebox pastebox = pasteboxRepository.getByHash(url).orElseThrow(() -> new NoSuchPasteboxException("no pasteboxes with hash " + url));
        if(isExpiredPastebox(pastebox)) throw new ExpiredPasteboxException("This pastebox has expired at "+pastebox.getExpiresAt());
        return entityMapper.mapPasteboxToPasteboxResponseDto(pastebox);
    }

    private boolean isExpiredPastebox(Pastebox pastebox) {
        return pastebox.getExpiresAt()!=null && pastebox.getExpiresAt().isBefore(LocalDateTime.now());
    }

    @Override
    public List<PasteboxResponseDto> getAllPublic() {
        return pasteboxRepository.getByPasteboxStatus(PasteboxStatus.PUBLIC).stream().filter(pastebox ->
                !this.isExpiredPastebox(pastebox)).map(entityMapper::mapPasteboxToPasteboxResponseDto).toList();
    }


    @Override
    public List<PasteboxResponseDto> getAll(){
        return pasteboxRepository.findAll().stream().map(entityMapper::mapPasteboxToPasteboxResponseDto).toList();
    }

    @Override
    public Page<PasteboxResponseDto> getAllPublic(Pageable pageable) {
        return pasteboxRepository.findAll(pageable).map(entityMapper::mapPasteboxToPasteboxResponseDto);
    }

}
