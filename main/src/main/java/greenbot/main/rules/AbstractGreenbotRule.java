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
package greenbot.main.rules;

import greenbot.rule.model.GreenbotRule;
import greenbot.rule.model.RuleInfo;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.*;

/**
 * @author Vinay Lodha
 */
public abstract class AbstractGreenbotRule implements GreenbotRule {

    protected String buildRuleId() {
        String[] tokens = splitByCharacterTypeCamelCase(getClass().getSimpleName());
        return lowerCase(join(tokens, '_'));
    }

    public RuleInfo buildRuleInfo(List<String> permissions) {
        return RuleInfo.builder()
                .id(buildRuleId())
                .docs("https://vinay-lodha.gitbook.io/greenbot/rules/" + buildRuleId())
                .permissions(permissions)
                .build();
    }

}
