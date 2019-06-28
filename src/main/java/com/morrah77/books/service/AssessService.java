package com.morrah77.books.service;

import com.morrah77.books.data.entity.Assess;
import com.morrah77.books.data.repository.AssessRepository;
import com.morrah77.books.model.BookModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssessService {

    @Autowired
    private AssessRepository assessRepository;

    public Assess getAssess(Long bookId, Long userId) {
        return assessRepository.findByBook_IdAndUser_Id(bookId, userId);
    }

    public Assess saveAssess(Assess assess) {
        return assessRepository.save(assess);
    }

    public void deleteAssess(Assess assess) {
        assessRepository.delete(assess);
    }

    public void deleteAssess(Long bookId, long userId) {
        assessRepository.deleteByBook_IdAndUser_Id(bookId, userId);
    }
}
