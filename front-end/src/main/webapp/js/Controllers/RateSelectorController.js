const RateSelectorController = (function() {

  //const init = function(htmlElement){
  //};


  const selectBin = function(htmlElement, binIndex) {

    $(htmlElement).siblings().removeClass("active");
    $(htmlElement).addClass("active");
    //console.log($(htmlElement));

    var ruleData = RuleService.get();

    const rulesFiltered = ruleData.rules.filter(rule => {
      const rate = rule.consequents.find(c => c.startsWith("rate"));
      const match = /rate=q(\d)/.exec(rate);
      const rateBinIndex = parseInt(match[1]);
      return rateBinIndex === binIndex + 1;
    });

    ruleData.rules = rulesFiltered;

    RuleContainer.setRules(ruleData);
  };

  const selectInitialBin = function() {
    $("ul.nav-tabs li").last().trigger('click');
  };

  return {
    selectBin: selectBin,
    selectInitialBin: selectInitialBin
  };

})();
