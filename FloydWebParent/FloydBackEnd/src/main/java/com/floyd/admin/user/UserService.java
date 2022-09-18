package com.floyd.admin.user;

import com.floyd.common.entity.Role;
import com.floyd.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static final int USERS_PER_PAGE = 4;

    public List<User> listUsers() {
        return (List<User>) userRepository.findAll();
    }

    public List<Role> listRoles() {
        return (List<Role>) roleRepository.findAll();
    }

    public User save(User user) {

        boolean isUpdatingUser = (user.getId() != null);

        if(isUpdatingUser) { // if updating user...
            var excistingUser = userRepository.findById(user.getId()).orElse(null);
            if(user.getPassword().isEmpty()) { // if password field is empty, use existing password...
                user.setPassword(excistingUser.getPassword());
            }
            else { // if password field is not empty, use new password...
                encodePassword(user);
            }
        }
        else { // if creating user...
            encodePassword(user);
        }

        return userRepository.save(user);
    }

    private void encodePassword(User user) {
        var encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    public boolean isEmailUnique(Integer id, String email) {
        var user = userRepository.getUserByEmail(email);

        if(id == null) { // if create new user...
            if(user != null) { // if another user is already using the email...
                return false;
            }
        }
        else { // if edit user...
            if(user.getId() != id) { // if editing user has different id...
                return false;
            }
        }

        return true;
    }

    public User get(Integer id) throws UserNotFoundException {
        try {
            return userRepository.findById(id).get();
        }
        catch (NoSuchElementException e) {
            throw new UserNotFoundException("Could not find any user with ID " + id);
        }
    }

    public void delete(Integer id) throws UserNotFoundException {
        Long count = userRepository.countById(id);
        if(count == null | count == 0) {
            throw new UserNotFoundException("Could not find any user with ID " + id);
        }
        userRepository.deleteById(id);
    }

    public void updateUserEnabledStatus(Integer id, boolean enabled) {
        userRepository.updateEnabledStatus(id, enabled);
    }

    public Page<User> listByPage(int pageNumber, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = (sortDir.equals("asc") ? sort.ascending() : sort.descending());
        Pageable pageable = PageRequest.of(pageNumber - 1, USERS_PER_PAGE, sort);

        if (keyword != null) {
            return userRepository.findAll(keyword, pageable);
        }

        return userRepository.findAll(pageable);
    }
}
