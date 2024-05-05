package com.mizore.easybuy.service.business;

import com.mizore.easybuy.service.base.ITbBanService;
import com.mizore.easybuy.utils.BloomFilter;
import jakarta.annotation.PostConstruct;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Setter
@Slf4j
public class BanService {

    @Value("${ban.bloomFilterSize}")
    private int bloomFilterSize = 1024;

    private BloomFilter<Integer> bloomFilter;

    @Autowired
    private ITbBanService banService;

    @PostConstruct
    public void init() {
        // 初始化布隆过滤器
        bloomFilter = new BloomFilter<>(bloomFilterSize);
        // 预加载被封禁的用户放进布隆过滤器   那啥时候refresh呢？目前只在服务重启时
        List<Integer> banUserIds = banService.listBanningUserId();
        bloomFilter.setBatch(banUserIds);
    }

    // 查指定用户是否处于封禁
    public boolean isBanning(int userId) {
        if (!bloomFilter.exist(userId)) {
            // bloom有不一定有，没有一定没有
            log.info("bloomFilter miss, return false directly. userId: {}", userId);
            return false;
        }
        // 出于hash冲突、已解封，可能true，查db
        return banService.isBanning(userId);
    }

    public void ban(int userId, long duration, String reason) {
        bloomFilter.set(userId);
        banService.ban(userId, duration, reason);
    }
}
