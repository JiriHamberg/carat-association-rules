/*
	Module SearchFormController

	Utilities for the rule search form.

*/
const SearchFormController = (function () {

	const init = function(formId) {

    AppService.getAppCounts(
      function(appCounts) {
        /*$.each(appCounts, function(app, count) {
          $("#select-app")
            .append($('<option>'), {value: app})
            .text(app + " (" + count + ")");
        });*/

        appCounts = _.pickBy(appCounts, function(count, app) {
          return count > 1000;
        });

        appCounts = _.map(_.toPairs(appCounts), function(pair) {return {app: pair[0], count: pair[1]}; });

        const $selectApp = $("#" + "select-app").selectize({
          maxOptions: 100,
          valueField: 'app',
          labelField: 'app',
          searchField: 'app',
          //uid: (item) => item.app,
          /*filterOptions: (options, search) => {
           const normalizedSearch = search.toLowerCase().trim()
           return _.take(options.filter((item) => {
            return item.app.toLowerCase().trim().indexOf(normalizedSearch) != -1
           }), 100)
          }*/
        });

        const control = $selectApp[0].selectize;

        control.addOption(appCounts);
      },
      function() {
        window.alert("Error: Could not load application info.");
      }
    );

    //const $selectApp = $("#" + "select-app").selectize();

		const $form = $("#" + formId);
		$form.on("submit", function(event) {
			event.preventDefault();

			var options = {};

      options["applicationName"] = $("#select-app").val();

			$("#" + formId + " input[type=number]").each(function() {
				options[$(this).attr('id')] = parseFloat($(this).val());
			});

      var excluded = [];

      $("#" + formId + " input[type=checkbox]:checked").each(function() {
        excluded.push($(this).val());
      });
      options.excluded = excluded;

			//$form.trigger('reset');
			fetchRules(options)
		});

	};

	const fetchRules = function(options) {
    $("#rule-list").empty(); //remove existing rule listing
		$("#rule-spinner").spin(); //run spinner while request is being processed
    //console.log(options);

    const successCallback = function(response) {
      $("#rule-spinner").data('spinner').stop(); //stop spinner

      const callback = function() {
        RateSelectorController.selectInitialBin();
      };

      RuleContainer.setRuleHeader(response, callback);
      RuleContainer.setRules(response, callback);
		};

    const errorCallback = function(response) {
      $("#rule-spinner").data('spinner').stop(); //stop spinner
			console.log("SearchFormController: failed to fetch data")
      console.log(response);
      alert("Backend responded with error code " + response.status);
    };

    RuleService.fetch(
      options,
      successCallback,
      errorCallback
    );

	};

	return {
		init: init
	};
})();
