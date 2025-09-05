package com.example.notes;

import jakarta.validation.constraints.NotBlank;

public class NoteDto {
    @NotBlank
    public String title;
    public String content;
}
