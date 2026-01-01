package br.com.emanueldias.Log.service;

import br.com.emanueldias.Log.dto.LogMessage;
import br.com.emanueldias.Log.model.LogMessageEntity;
import br.com.emanueldias.Log.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    public void persistMessage(LogMessage dto) {
        LogMessageEntity logMessage = new LogMessageEntity(dto.getLevel(), dto.getOperation(), dto.getTimestamp());
        System.out.println(logMessage);
        logRepository.save(logMessage);
    }
}
