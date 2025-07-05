package com.tutorXpert.tutorxpert_backend.service.impl;

import com.tutorXpert.tutorxpert_backend.domain.po.Message;
import com.tutorXpert.tutorxpert_backend.mapper.MessageMapper;
import com.tutorXpert.tutorxpert_backend.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageServiceImpl implements IMessageService {
    @Autowired
    private MessageMapper messageMapper;

    @Override
    public List<Message> getAllMessages() {
        return messageMapper.selectList(null);
    }

    @Override
    public Message sendMessage(Message message) {
        message.setSentAt(LocalDateTime.now());
        messageMapper.insert(message);
        return message;
    }
}

