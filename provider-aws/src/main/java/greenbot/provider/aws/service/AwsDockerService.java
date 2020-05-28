/*
 * Copyright 2020 Vinay Lodha (https://github.com/vinay-lodha)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package greenbot.provider.aws.service;

import greenbot.provider.service.DockerService;
import greenbot.rule.model.AnalysisConfidence;
import greenbot.rule.model.cloud.Compute;
import greenbot.rule.model.cloud.PossibleUpgradeInfo;
import greenbot.rule.model.cloud.Tag;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

import static greenbot.provider.aws.utils.AwsTags.ELASTIC_BEANSTALK_APP_NAME;

/**
 * @author Vinay Lodha
 */
@Service
@AllArgsConstructor
public class AwsDockerService implements DockerService {

    @Override
    public Map<Compute, List<PossibleUpgradeInfo>> checkUpgradePossibility(List<Compute> computes) {
        HashMap<String, List<Compute>> beanstalkComputeMap = new HashMap<>();
        computes.forEach(compute -> {
            Tag tag = compute.getTags().get(ELASTIC_BEANSTALK_APP_NAME);
            if (tag != null) {
                beanstalkComputeMap.putIfAbsent(tag.getValue(), new ArrayList<>());
                beanstalkComputeMap.get(tag.getValue()).add(compute);
            }
        });

        Map<Compute, List<PossibleUpgradeInfo>> retVal = new HashMap<>();
        for (String key : beanstalkComputeMap.keySet()) {
            Compute compute = beanstalkComputeMap.get(key).get(0);
            String appName = compute.getTags().get(ELASTIC_BEANSTALK_APP_NAME).getValue();
            PossibleUpgradeInfo possibleUpgradeInfo = PossibleUpgradeInfo.fromResource(compute)
                    .confidence(AnalysisConfidence.MEDIUM)
                    .reason(String.format("Consider migrating beanstalk application %s to Amazon ECS", appName))
                    .resourceId(appName)
                    .build();
            retVal.put(compute, Collections.singletonList(possibleUpgradeInfo));
        }
        return retVal;
    }

}
