package myscores.services;

import myscores.domain.Gambler;
import myscores.repositories.GamblerRepository;
import myscores.repositories.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class GamblerService extends Service<Gambler> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GamblerService.class);

    @Inject
    private GamblerRepository repository;

    @Override
    public Gambler get(int id) {
        LOGGER.info("Get gambler with id {}", id);
        try {
            return repository.read(id);
        } catch (Exception e) {
            throw new ServiceException("An error occurred while getting gambler with id " + id, e);
        }
    }

    @Override
    public List<Gambler> find() {
        LOGGER.info("Find gamblers");
        try {
            return repository.find();
        } catch (Exception e) {
            throw new ServiceException("An error occurred while finding gamblers", e);
        }
    }

    @Override
    public void register(Gambler gambler) {
        LOGGER.info("Register gambler {}", gambler.getUsername());
        gambler.setActive(Boolean.FALSE);
        try {
            repository.create(gambler);
        } catch (Exception e) {
            throw new ServiceException("An error occurred while registering gambler " + gambler.getUsername(), e);
        }
    }

    @Override
    public void change(Gambler gambler) {
        LOGGER.info("Change gambler {} with id {}", gambler.getUsername(), gambler.getId());
        try {
            repository.update(gambler);
        } catch (Exception e) {
            throw new ServiceException("An error occurred while changing gambler " + gambler.getUsername() + " with id " + gambler.getId(), e);
        }
    }

    @Override
    public void delete(int id) {
        LOGGER.info("Delete gambler with id {}", id);
        try {
            repository.delete(id);
        } catch (Exception e) {
            throw new ServiceException("An error occurred while deleting gambler with id " + id, e);
        }
    }

    public void activate(int id) {
        Gambler gambler;
        try {
            gambler = repository.read(id);
        } catch (Exception e) {
            throw new ServiceException("An error occurred while getting gambler with id " + id, e);
        }
        if (gambler == null) {
            throw new ServiceException("No gambler found for id " + id);
        } else if (gambler.isActive()) {
            throw new ServiceException("Gambler " + gambler.getUsername() + " with id " + id + " is already active");
        } else {
            LOGGER.info("Activate gambler {} with id {}", gambler.getUsername(), id);
            gambler.setActive(Boolean.TRUE);
            try {
                repository.update(gambler);
            } catch (RepositoryException e) {
                throw new ServiceException("An error occurred while activating gambler " + gambler.getUsername() + " with id " + id, e);
            }
        }
    }
}
