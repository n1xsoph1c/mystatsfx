package com.ghostcompany.mystats.Service;

import com.ghostcompany.mystats.Model.Account.AccountGroup;

import java.util.ArrayList;
import java.util.List;

public class Account extends com.ghostcompany.mystats.Model.Account.Account {

    public Account() {
        super();
    }

    public List<com.ghostcompany.mystats.Model.Account.Account> getAccounts() {
        List<com.ghostcompany.mystats.Model.Account.Account> accounts = new ArrayList<>();
        // fetch accounts from SQL
        return accounts;
    }
}
