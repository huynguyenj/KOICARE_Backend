package group7.se1876.kcs_backend.service;

import group7.se1876.kcs_backend.repository.KoiFishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KoiFishService {

    @Autowired
    private KoiFishRepository koiFishRepository;

}
