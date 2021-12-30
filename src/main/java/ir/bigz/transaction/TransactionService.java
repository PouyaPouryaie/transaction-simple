package ir.bigz.transaction;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Human getHuman(Long id) {
        Optional<Human> byId = transactionRepository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        }
        throw new IllegalStateException("user not found");
    }

    public Human insertHuman(Human human) {
        Human save = transactionRepository.save(human);
        System.out.println(save);
        return save;
    }

    @Transactional //for update dont need call save method from repository
    public Human updateHuman(Long id, Human human) {

        Human humanById = this.getHuman(id);
        System.out.println(humanById.toString());

        humanById.setActive(human.isActive());
        humanById.setNationalCode(human.getNationalCode());
        humanById.setAge(human.getAge());
        humanById.setFirstName(human.getFirstName());
        humanById.setLastName(human.getLastName());

        return this.getHuman(id);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean changeState(Long id, boolean state) throws Exception{
        Human humanById = this.getHuman(id);
        int i = transactionRepository.updateHuman(humanById.getId(), state);
        System.out.println("result ************ " + i);
        if(i == 1){
            return true;
        }
        throw new Exception("data is not unique");
    }

    /**
     * this method show how hibernate cache behavior when some data fetch and change it in some another transaction
     * @param id
     * @param human
     * @return
     */
    public Human getAndAdd(Long id, Human human){

        Human humanRetrieve = this.getHuman(id);
        humanRetrieve.setFirstName("changeName");

        return insertHuman(human);

    }
}
