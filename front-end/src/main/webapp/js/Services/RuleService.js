const RuleService = (function() {

  var rules = null;

  const fetchRules = function(options, successCallback, errorCallback) {
    $.ajax({
			url: contextPath + "/spark-submit",
			type: "POST",
			dataType: "json",
      contentType: 'application/json; charset=utf-8',
			data: JSON.stringify(options),
      success: function(response) {
        rules = response;
        successCallback(response);
      },
      error: errorCallback
    });
  };

  const getRules = function() {
    if(rules === null) {
       throw "getRules called before fetchRules was finished";
    }
    // return a deep copy
    return JSON.parse(JSON.stringify(rules));
  };

  return {
    fetch: fetchRules,
    get: getRules
  };

})();
