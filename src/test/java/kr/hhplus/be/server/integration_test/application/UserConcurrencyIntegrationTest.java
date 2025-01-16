package kr.hhplus.be.server.integration_test.application;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.integration_test.application.set_up.UserSetUp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserConcurrencyIntegrationTest extends BaseIntegrationTest{

    @Autowired
    private UserSetUp userSetUp;

    @Autowired
    private UserService userService;

    @Test
    void 동일한_유저가_동시에_5번_잔액_충전을_했을_때_예상금액과_같은지_성공_검증() throws InterruptedException {
        //given
        int threadCount = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        User savedUser = userSetUp.saveUser("김화진", BigDecimal.valueOf(10000));

        //when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                User user = userService.charge(savedUser.getId(), BigDecimal.valueOf(1000));
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.SECONDS);

        //then
        BigDecimal pointBalance = userService.findById(savedUser.getId()).getPointBalance();
        assertEquals(BigDecimal.valueOf(15000.00).setScale(2, RoundingMode.DOWN), pointBalance.setScale(2, RoundingMode.DOWN));
    }
}
