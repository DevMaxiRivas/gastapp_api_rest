package com.app.dto.v1.profile;

import com.app.enums.user.CurrencyType;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Getter
public class ProfileUpdateDTO {
    private CurrencyType currency;
    private BigDecimal currentBudget;
}
