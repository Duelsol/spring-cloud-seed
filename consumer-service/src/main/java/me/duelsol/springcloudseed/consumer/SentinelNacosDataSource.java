package me.duelsol.springcloudseed.consumer;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.nacos.api.PropertyKeyConst;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Properties;

/**
 * @author 冯奕骅
 */
@Component
public class SentinelNacosDataSource {

    @Value("${spring.cloud.nacos.discovery.server-addr}")
    private String serverAddr;

    private static final String DATA_ID = "consumer-service";

    private static final String FLOW_RULE_GROUP_ID = "sentinel-flow-rule";

    private static final String DEGRADE_RULE_GROUP_ID = "sentinel-degrade-rule";

    private static final String NAMESPACE = "";

    @PostConstruct
    public void loadRules() {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
        properties.put(PropertyKeyConst.NAMESPACE, NAMESPACE);

        ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new NacosDataSource<>(
                properties, FLOW_RULE_GROUP_ID, DATA_ID,
                source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {}));
        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());

        ReadableDataSource<String, List<DegradeRule>> degradeRuleDataSource = new NacosDataSource<>(
                properties, DEGRADE_RULE_GROUP_ID, DATA_ID,
                source -> JSON.parseObject(source, new TypeReference<List<DegradeRule>>() {}));
        DegradeRuleManager.register2Property(degradeRuleDataSource.getProperty());
    }

}
