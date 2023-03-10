package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.OriginAccount;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;


@PreAuthorize("isAuthenticated()")
@RestController
public class TransferController {

    private TransferDao transferDao;
    private UserDao userDao;
    private Principal principal;

    public TransferController(TransferDao transferDao, UserDao userDao) {
        this.transferDao = transferDao;
        this.userDao = userDao;

    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "transfer", method = RequestMethod.POST)
    public void transferCreated(@RequestBody Transfer transfer) {
        if (!(transferDao.createTransfer(transfer.getTransferStatusId(), transfer.getTransferTypeId(), transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transfer failed.");
        }
    }

// TODO fix the negative balance and implement transferAllowed
    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(path = "accounts/{id}/transfer/send", method = RequestMethod.POST)
    public void sendMoney(@PathVariable int id, @RequestBody OriginAccount originAccount) {
        if (transferDao.transferAllowed(originAccount.getAmount(), originAccount.getAccountFrom())) {
            transferDao.subtractTransferAmount(originAccount.getAmount(), originAccount.getAccountFrom());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transfer failed.");
        }
    }



}
