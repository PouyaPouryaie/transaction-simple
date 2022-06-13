package ir.bigz.transaction;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/transaction")
@AllArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getHuman(@PathVariable("id") Long id){
        Human result = transactionService.getHuman(id);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping(path = "byAge/{age}")
    public ResponseEntity<?> getHumanByAge(@PathVariable("age") Long age){
        List<Human> result = transactionService.getHumanByAgeUseMapResult(age);
        return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
    }

    @PostMapping(path = "/")
    public ResponseEntity<?> insertHuman(@RequestBody Human human){
        Human result = transactionService.insertHuman(human);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<?> updateHuman(@PathVariable("id") Long id, @RequestBody Human human){
        Human result = transactionService.updateHuman(id, human);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PostMapping(path = "/{id}")
    public ResponseEntity<?> changeState(@PathVariable("id") Long id, @RequestParam boolean state){
        try {
            boolean result = transactionService.changeState(id, state);
            if(result){
                return new ResponseEntity<>(transactionService.getHuman(id), HttpStatus.CREATED);
            }
        }catch (Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("update not done", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping(path = "getAndAdd/{id}")
    public ResponseEntity<?> getAndAdd(@PathVariable("id") Long id, @RequestBody Human human){
        try {
            Human result = transactionService.getAndAdd(id, human);
            if(Objects.nonNull(result)){
                return new ResponseEntity<>(result, HttpStatus.CREATED);
            }
        }catch (Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("insert not done", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
