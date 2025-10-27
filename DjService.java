package za.ac.cput.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.DJ;
import za.ac.cput.repository.DJRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DjService implements IDjService {

    private final DJRepository repository;

    @Autowired
    public DjService(DJRepository repository) {
        this.repository = repository;
    }

    @Override
    public DJ create(DJ dj) {
        return repository.save(dj);
    }

    @Override
    public DJ read(Long djId) {
        Optional<DJ> optionalDj = repository.findById(djId);
        return optionalDj.orElse(null);
    }

    @Override
    public DJ update(DJ dj) {
        if (repository.existsById(dj.getDjId())) {
            return repository.save(dj);
        }
        return null;
    }

    @Override
    public boolean delete(Long djId) {
        if (repository.existsById(djId)) {
            repository.deleteById(djId);
            return true;
        }
        return false;
    }

    @Override
    public List<DJ> getAll() {
        return repository.findAll();
    }
}