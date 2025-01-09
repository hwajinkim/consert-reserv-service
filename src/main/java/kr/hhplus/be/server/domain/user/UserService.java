package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.application.dto.user.UserBalanceParam;
import kr.hhplus.be.server.common.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PointHistoryRepository pointHistoryRepository;

    public User findById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("사용자의 정보가 없습니다."));
        return user;
    }


    @Transactional
    public User charge(Long userId, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("사용자의 정보가 없습니다."));
        User userUpdate = user.charge(amount);
        User userSave = userRepository.save(userUpdate);

        PointHistory pointHistory = PointHistory.builder()
                .userId(userId)
                .transMethod(TransMethod.CHARGE)
                .transAmount(amount)
                .balanceBefore(user.getPointBalance())
                .balanceAfter(userUpdate.getPointBalance())
                .build();

        PointHistory pointHistorySave = pointHistoryRepository.save(pointHistory);
        return userSave;
    }

    @Transactional
    public User use(Long userId, BigDecimal amount){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("사용자의 정보가 없습니다."));
        User userUpdate = user.use(user.getPointBalance(), amount);
        User userSave = userRepository.save(userUpdate);

        PointHistory pointHistory = PointHistory.builder()
                .userId(userId)
                .transMethod(TransMethod.USE)
                .transAmount(amount)
                .balanceBefore(user.getPointBalance())
                .balanceAfter(userUpdate.getPointBalance())
                .build();

        PointHistory pointHistorySave = pointHistoryRepository.save(pointHistory);
        return userSave;
    }
}
