package com.s_giken.traning.webapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.s_giken.traning.webapp.model.Charge;
import com.s_giken.traning.webapp.model.ChargeSearchCondition;
import com.s_giken.traning.webapp.repository.ChargeRepository;

@Service
public class ChargeServiceImpl implements ChargeService {
    @Autowired
    private ChargeRepository chargeRepository;

    @Override
    public List<Charge> findAll() {
        return chargeRepository.findAll();
    }

    @Override
    public Optional<Charge> findById(int chargeId) {
        return chargeRepository.findById(chargeId);
    }

    @Override
    public List<Charge> findByCondition(ChargeSearchCondition condition) {
        List<Charge> result = null;
        if (condition.getName() == null || condition.getName().isEmpty()) {
            result = chargeRepository.findAll();
        } else {
            result = chargeRepository.findByNameLike("%" + condition.getName() + "%");
        }
        return result;
    }

    @Override
    @Transactional
    public void save(Charge charge) {
        var c = chargeRepository.findById(charge.getChargeId());
        if (c.isPresent()) {

        }
        chargeRepository.save(charge);
    }

    @Override
    public void deleteById(int chargeId) {
        chargeRepository.deleteById(chargeId);
    }
}
