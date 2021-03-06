package com.opera.service.impl;

import com.opera.dao.StageDao;
import com.opera.model.Stage;
import com.opera.service.StageService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StageServiceImpl implements StageService {
    private final StageDao stageDao;

    @Autowired
    public StageServiceImpl(StageDao stageDao) {
        this.stageDao = stageDao;
    }

    @Override
    public Stage add(Stage stage) {
        return stageDao.add(stage);
    }

    @Override
    public List<Stage> getAll() {
        return stageDao.getAll();
    }

    @Override
    public Stage get(Long stageId) {
        return stageDao.get(stageId).orElseThrow(() ->
                new RuntimeException("Stage for id: " + stageId + " not found."));
    }
}
