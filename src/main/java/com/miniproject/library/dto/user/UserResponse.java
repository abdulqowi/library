package com.miniproject.library.dto.user;

import com.miniproject.library.dto.anggota.AnggotaResponse;
import com.miniproject.library.entity.Anggota;
import lombok.Data;

import java.util.List;

@Data

public class UserResponse {
    private Integer id;
    private String username;

    private AnggotaResponse anggota;
    private List<UserBookResponse> book;

}
