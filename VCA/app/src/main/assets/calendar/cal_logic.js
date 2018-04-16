/**
 * [description]
 * @return {[type]} [description]
 */
$('.datepicker').pickadate(
  {
    format: 'yyyy-mm-dd',
    formatSubmit: 'yyyy-mm-dd'
  }
);

$('.timepicker').pickatime();

$("#btnAdd").click(function() {

  $('#cal-menu').hide();
  $('#cal-add').show();

});

$("#btnAddBack").click(function() {

  $('#cal-menu').show();
  $('#cal-add').hide();

});
