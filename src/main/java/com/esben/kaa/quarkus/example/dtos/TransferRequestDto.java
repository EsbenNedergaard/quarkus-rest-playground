package com.esben.kaa.quarkus.example.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "TransferRequestDto", description = "Transfer request DTO object")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequestDto {
    public Long sourceAccountId;
    public Long destinationAccountId;
    public int amount;
}
