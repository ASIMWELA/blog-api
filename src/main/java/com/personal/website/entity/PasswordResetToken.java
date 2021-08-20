package com.personal.website.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name="password_reset_token")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class PasswordResetToken extends BaseEntity
{
    @Column(nullable=false, unique = true)
    String token;

    @Column(nullable = false)
    Date expiryDate;

    @Column(nullable = false, name = "is_expired")
    boolean isExpired;

    @OneToOne(targetEntity = UserEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    UserEntity user;

    public PasswordResetToken(String toString) {
    }

    public void setExpiryDate(Date expiryDate)
    {
        this.expiryDate = expiryDate;
    }

    public void setExpiryDate(int minutes)
    {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, minutes);
        this.expiryDate = now.getTime();
    }

    public boolean isExpired()
    {
        return new Date().after(this.expiryDate);
    }

}
