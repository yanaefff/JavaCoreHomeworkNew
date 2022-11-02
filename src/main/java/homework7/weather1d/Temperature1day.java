package homework7.weather1d;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Metric",
        "Imperial"
})
@Generated("jsonschema2pojo")
public class Temperature1day {

    @JsonProperty("Metric")
    private Metric1day metric;
    @JsonProperty("Imperial")
    private Imperial1day imperial;

    @JsonProperty("Metric")
    public Metric1day getMetric() {
        return metric;
    }

    @JsonProperty("Metric")
    public void setMetric(Metric1day metric) {
        this.metric = metric;
    }

    @JsonProperty("Imperial")
    public Imperial1day getImperial() {
        return imperial;
    }

    @JsonProperty("Imperial")
    public void setImperial(Imperial1day imperial) {
        this.imperial = imperial;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        sb.append("metric");
        sb.append('=');
        sb.append(((this.metric == null) ? "<null>" : this.metric));
        sb.append(',');
        sb.append("imperial");
        sb.append('=');
        sb.append(((this.imperial == null) ? "<null>" : this.imperial));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
