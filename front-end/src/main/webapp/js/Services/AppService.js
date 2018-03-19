const AppService = (function() {

  var appCounts = undefined;

  const getAppCounts = function(successCallback, errorCallback) {

    if(appCounts !== undefined) {
      successCallback(appCounts);
      return;
    }

    $.ajax({
			url: contextPath + "/app-counts",
			type: "GET",
			dataType: "json",
      success: successCallback,
      error: errorCallback
    });

  };

  return {
    getAppCounts: getAppCounts
  };

}());
