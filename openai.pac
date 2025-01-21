var FindProxyForURL = function(init, profiles) {
    return function(url, host) {
        "use strict";
        var result = init, scheme = url.substr(0, url.indexOf(":"));
        do {
            result = profiles[result];
            if (typeof result === "function") result = result(url, host, scheme);
        } while (typeof result !== "string" || result.charCodeAt(0) === 43);
        return result;
    };
}("+auto switch", {
    "+auto switch": function(url, host, scheme) {
        "use strict";
        if (/chatgpt\.com$/.test(host)) return "+10.100.1.1:8118";
        if (/^cdn\.oaistatic\.com$/.test(host)) return "+10.100.1.1:8118";
        if (/openai/.test(host)) return "+10.100.1.1:8118";
        return "+__ruleListOf_auto switch";
    },
    "+__ruleListOf_auto switch": "DIRECT",
    "+10.100.1.1:8118": function(url, host, scheme) {
        "use strict";
        if (/^127\.0\.0\.1$/.test(host) || /^::1$/.test(host) || /^localhost$/.test(host)) return "DIRECT";
        return "PROXY 10.100.1.1:8118";
    }
});
