package com.example.JBoard.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class PaginationService {

    private static final int BAR_LENGTH = 5;

    public List<Integer> getPaginationBarNumbers(int currentPageNumber, int totalPages) {
        int startNumber = Math.max(currentPageNumber - (BAR_LENGTH/2), 0);
        int endNumber = Math.min(startNumber + BAR_LENGTH, totalPages);

        return IntStream.range(startNumber, endNumber).boxed().toList();    // IntStream 원시 타입을 박싱하여 stream -> List<Integer>로 변환해주었다.
    }

    public int currentBarLength() { // 페이징네이션 바 길이
        return BAR_LENGTH;
    }
}
