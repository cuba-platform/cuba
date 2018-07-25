package spec.cuba.core.query_conditions;

import com.haulmont.cuba.core.global.queryconditions.PropertyCondition;

import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SampleRestCondition extends PropertyCondition {

    public static final Pattern PARAMETER_PATTERN = Pattern.compile("\\$\\{([\\w.$]+)}");

    public SampleRestCondition(String param) {
        super(Collections.singletonList(new Entry("param", param)));
    }

    @Override
    protected void parseParameters() {
        for (Entry entry : entries) {
            Matcher matcher = PARAMETER_PATTERN.matcher(entry.value);
            while (matcher.find()) {
                String parameter = matcher.group(1);
                if (!parameters.contains(parameter))
                    parameters.add(parameter);
            }
        }
    }
}
