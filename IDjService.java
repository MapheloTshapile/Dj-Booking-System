package za.ac.cput.service;

import za.ac.cput.domain.DJ;

import java.util.List;

public interface IDjService extends IService<DJ, Long> {
    List<DJ> getAll();
}