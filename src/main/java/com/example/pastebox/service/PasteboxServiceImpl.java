package com.example.pastebox.service;

import com.example.pastebox.auth.repository.UserRepository;
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
import org.springframework.security.access.AccessDeniedException;
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
    private final UserRepository userRepository;

    @Transactional
    @Override
    public PasteboxResponseDto createPastebox(PasteboxRequestDto dto, String username) {
        Pastebox pastebox = entityMapper.mapPasteboxDtoToPastebox(dto);
        pastebox.setUser(userRepository.findByUsername(username).get());
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

    @Override
    public List<PasteboxResponseDto> getByUserUsername(String userUsername) {
        return pasteboxRepository.getByUserUsername(userUsername).stream().map(entityMapper::mapPasteboxToPasteboxResponseDto).toList();
    }

    @Override
    @Transactional
    public void deletePasteboxByUser(String url, String username) {
        Pastebox pastebox = pasteboxRepository.getByHash(url).orElseThrow(() -> new NoSuchPasteboxException("no pasteboxes with hash " + url));
        if(!pastebox.getUser().getUsername().equals(username)) throw new AccessDeniedException("Пользователь не имеет доступа к ресурсу {"+url+"}");
        pasteboxRepository.delete(pastebox);
    }

    @Override
    @Transactional
    public void deletePastebox(String url) { //для админов
        Pastebox pastebox = pasteboxRepository.getByHash(url).orElseThrow(() -> new NoSuchPasteboxException("no pasteboxes with hash " + url));
        pasteboxRepository.delete(pastebox);
    }

}
