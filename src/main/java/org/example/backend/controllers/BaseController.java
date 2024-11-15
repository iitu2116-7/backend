package org.example.backend.controllers;

import org.example.backend.services.utilServices.ReceiveToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.util.Map;

public abstract class BaseController {

    @Autowired
    protected ReceiveToken receiveToken;

    protected Map<String, String> data;

    @ModelAttribute
    public void initData() {
        this.data = receiveToken.tokenData();
    }
}


