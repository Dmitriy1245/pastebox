package com.example.pastebox.entity;


import com.example.pastebox.auth.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "pasteboxes")
@NoArgsConstructor
@EqualsAndHashCode(exclude = "expiresAt")
public class Pastebox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String data;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PasteboxStatus pasteboxStatus;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt; //Если null, то бессрочно

    private String hash;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Pastebox(String data, PasteboxStatus pasteboxStatus, LocalDateTime createdAt, LocalDateTime expiresAt) {
        this.data = data;
        this.pasteboxStatus = pasteboxStatus;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }
}
