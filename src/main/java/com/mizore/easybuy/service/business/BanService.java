package com.mizore.easybuy.service.business;

import com.mizore.easybuy.utils.BloomFilter;
import jakarta.annotation.PostConstruct;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Setter
public class BanService {

    @Value("ban.bloomFilterSize")
    private int bloomFilterSize = 1000;

    private BloomFilter<Integer> bloomFilter;

    @Autowired
//    private ITbBlackHouseService blackHouseService;

    @PostConstruct
    public void init() {
        // 初始化布隆过滤器
        bloomFilter = new BloomFilter<>(bloomFilterSize);
        // 预加载被封禁的用户放进布隆过滤器   那啥时候refresh呢？目前只在服务重启时

    }
}
