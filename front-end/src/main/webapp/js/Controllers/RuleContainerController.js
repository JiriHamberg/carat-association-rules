const RuleContainer = (function() {

  var element = null;
  var ruleHeader = null;
  var ruleList = null;

  const init = function(htmlElement) {
    element = $(htmlElement);
    element.append('<div id="rule-header"></div>');
    ruleHeader = $("#rule-header");
    element.append('<div id="rule-list"></div>');
    ruleList = $("#rule-list");
  };

  const setRuleHeader = function(rules, callback) {
    if(ruleHeader === null) {
      throw "setRules called before init";
    }
    $.get(contextPath + "/mustache/rules/rule-header.html", function(template) {
      const compiled = Handlebars.compile(template);
      const rendered = compiled(rules);
			ruleHeader.html(rendered);
      if(callback !== undefined) {
        callback();
      }
		});
  };

  const setRules = function(rules, callback) {
    if(ruleList === null) {
      throw "setRules called before init";
    }

    $.get(contextPath + "/mustache/rules/rule-list.html", function(template) {
      const compiled = Handlebars.compile(template);
      const rendered = compiled(rules);
			ruleList.html(rendered);
      if(callback !== undefined) {
       callback();
      }
		});
  };

  return {
    init: init,
    setRuleHeader: setRuleHeader,
    setRules: setRules
  };

})();
