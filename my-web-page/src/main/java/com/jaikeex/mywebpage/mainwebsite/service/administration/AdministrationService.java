package com.jaikeex.mywebpage.mainwebsite.service.administration;

import com.jaikeex.mywebpage.config.connection.ServiceRequest;
import com.jaikeex.mywebpage.mainwebsite.connection.MwpServiceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdministrationService {

    private final ServiceRequest serviceRequest;

    @Autowired
    public AdministrationService(MwpServiceRequest serviceRequest) {
        this.serviceRequest = serviceRequest;
    }
}
