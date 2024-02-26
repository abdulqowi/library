package com.miniproject.library.service;

import com.miniproject.library.dto.anggota.AnggotaResponse;
import com.miniproject.library.dto.user.UserBookResponse;
import com.miniproject.library.dto.user.UserRequest;
import com.miniproject.library.dto.user.UserResponse;
import com.miniproject.library.entity.Anggota;
import com.miniproject.library.entity.Book;
import com.miniproject.library.entity.Loan;
import com.miniproject.library.entity.User;
import com.miniproject.library.exception.ResourceNotFoundException;
import com.miniproject.library.repository.LoanRepository;
import com.miniproject.library.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final String USER_NOT_FOUND = "User Not Found";

    private final UserRepository userRepository;
    private final ModelMapper mapper = new ModelMapper();
    private final LoanRepository loanRepository;

    public List<UserResponse>getAll(){
        List<User> userList=userRepository.findAll();
        return userList.stream().map(user -> mapper.map(user,UserResponse.class)).toList();
    }

    public UserResponse getById(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Anggota anggota = user.getAnggota();
            Optional<Integer> userLoan = loanRepository.findLoanAnggota(id);
            if (userLoan.isPresent()) {
                List<Integer> bookList = userLoan.stream().toList();
                UserResponse userResponse = new UserResponse();
                userResponse.setId(user.getId());
                userResponse.setUsername(user.getUsername());
                userResponse.setAnggota(mapper.map(anggota, AnggotaResponse.class));
                userResponse.setBook(mapper.map(bookList, List.class));
                return userResponse;
            } else {
                throw new ResourceNotFoundException("Loan not found for user with id: " + id);
            }
        } else {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
    }

    public void deleteById(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException(USER_NOT_FOUND);
        }
    }
    public UserResponse updateById(Integer id,@Valid UserRequest userRequest) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.map(userRequest, existingUser);
            userRepository.save(existingUser);

            return modelMapper.map(existingUser, UserResponse.class);
        } else {
            throw new ResourceNotFoundException(USER_NOT_FOUND);
        }
    }
}
