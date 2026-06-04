package com.app.dto.v1.profile;

import com.app.enums.user.CurrencyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Getter
public class ProfileCreateDTO {
        @NotNull
        private CurrencyType currency;

        @NotNull
        private BigDecimal currentBudget;

}
