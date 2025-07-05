package com.tutorXpert.tutorxpert_backend.service;

import com.tutorXpert.tutorxpert_backend.domain.po.Message;

import java.util.List;

public interface IMessageService {
    List<Message> getAllMessages();
    Message sendMessage(Message message);
}
