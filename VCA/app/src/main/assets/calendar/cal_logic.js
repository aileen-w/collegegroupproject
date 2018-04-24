/**
 * [description]
 * @return {[type]} [description]
 */

//var apiUrl = "http://localdev:15000/api.php"; // development
 var apiUrl = "http://cit-project.hopto.org:15000/api.php"; // production

//var local_user = "device01"; //development
 var local_user = null; //production

var local_id = null;
var date_prev = null;
var date_next = null;
var date_display = null;

 /***********************************************
          Date/time picker
  **********************************************/
$('.datepicker').pickadate(
  {
    format: 'yyyy-m-d',
    formatSubmit: 'yyyy-m-d'
  }
);

$('.timepicker').pickatime(
  {
    format: 'HH:i',
    formatLabel: 'HH:i',
  }
);

/***********************************************
                      ADD
  **********************************************/

  $("#btnAdd").click(function() {

    $('#cal-menu').hide();
    $('#cal-add').show();

  });

$("#btnAddBack").click(function() {

  $('#cal-menu').show();
  $('#cal-add').hide();
  resetFields();

});


// add button
$("#btnAddaction").click(function() {

    $('#add_error').hide();
    $('#add_success').hide();
    $('#add_warning').hide();

    if( !$("#add_title").val() ||
        !$("#add_desc").val() ||
        !$("#add_date").val() ||
        !$("#add_from").val() ||
        !$("#add_to").val() ||
        (Date.parse('01/01/2011 ' + $("#add_from").val() + ":00") > Date.parse('01/01/2011 ' + $("#add_to").val() + ":00"))
      ){
      $('#add_warning').show();
      return false;
    }

    var d = {
        user: local_user,
        title: $("#add_title").val(),
        desc: $("#add_desc").val(),
        date: $("#add_date").val(),
        t_from: $("#add_from").val(),
        t_to: $("#add_to").val(),
        action: "new"
    };
    var dd = {
      "svc" : "calendar",
      "dev" : local_user,
      "msg" : JSON.stringify(d)
    };

    $.ajax({
        url: apiUrl,
        data: JSON.stringify(dd),
        headers: {  'Access-Control-Allow-Origin': '*' },
        error: function(e, jqXHR, textStatus) {
            // console.log('error: ', e);
            // console.log('jqXHR: ', jqXHR);
            // console.log('textStatus: ', textStatus);
            $('#add_error').show();
        },
        dataType: 'jsonp',
        crossDomain: true,
        success: function(data) {

            if(data["status"] == "Ok")
            {
                resetFields();
                $('#add_error').hide();
                $('#add_success').show();
                // $("#add_title").val("");
                // $("#add_desc").val("");
                // $("#add_date").val("");
                // $("#add_from").val("");
                // $("#add_to").val("");

                setTimeout(function(){
                    $('#add_success').hide();
                }, 3000);
            }
            else if (data["status"] == "Error") {
              $('#add_error').show();
            }
        },
        type: 'POST'
    });

});

/***********************************************
                    EDIT
  **********************************************/


/* edit */
$("#btnEdit").click(function() {

  $('#cal-menu').hide();
  $('#cal-edit-select').show();

});
$("#btnEditSubmitDate").click(function() {

  $('.edit_error').hide();
  $('.edit_success').hide();
  $('.edit_warning').hide();

  if( !$("#edit_select").val()){
    $('.edit_warning').show();
    return false;
  }

  var d = {
      user: local_user,
      date: $("#edit_select").val(),
      action: "view"
  };
  var dd = {
    "svc" : "calendar",
    "dev" : local_user,
    "msg" : JSON.stringify(d)
  };

  $.ajax({
      url: apiUrl,
      data: JSON.stringify(dd),
      headers: {  'Access-Control-Allow-Origin': '*' },
      error: function(e, jqXHR, textStatus) {
          $('.edit_error').show();
      },
      dataType: 'jsonp',crossDomain: true,
      success: function(data) {
        var data = JSON.parse(JSON.stringify(data));

          if(data["status"] == "Ok")
          {
            $('.edit_error').hide();
            $('.edit_success').hide();
            $('.edit_warning').hide();
            data['msg'] = JSON.parse(JSON.stringify(data['msg']));
            data['msg'] = (JSON.parse(data['msg']));
            var content = "";
            if(data["msg"].length>0)
            {
                for(var i=0; i<data["msg"].length; i++){
                  var o = data["msg"][i];
                  var tmp = '<table class="table table-bordered table-sm">' +
                    '<tbody>' +
                      '<tr>' +
                        '<td>'+o.cal_date+'</td>' +
                        '<td>'+o.time_from + '-' + o.time_to+'</td>' +
                      '</tr>' +
                      '<tr>' +
                        '<td colspan="2">'+o.title+'</td>' +
                      '</tr>' +
                      '<tr>' +
                        '<td colspan="2">'+o.cal_desc+'</td>' +
                      '</tr>' +
                      '<tr>' +
                        '<td colspan="2"><button type="button" class="btn btn-secondary btn-block btnEditEntry" value="'+o.id+'">Edit <i class="far fa-edit"></i></button></td>' +
                      '</tr>' +
                    '</tbody>' +
                  '</table>';
                  content = content + tmp;
                }
                $("#edit-view-list").html(content);
            }

          }
          else if (data["status"] == "Error") {
            $('.edit_error').show();
          }
      },
      type: 'POST'
  });

  $('#cal-edit-select').hide();
  $('#cal-edit-view').show();
  // $('#cal-edit-edition').show();

});

$('#edit-view-list').on('click', '.btnEditEntry', function() {

  $('.edit_error').hide();
  $('.edit_success').hide();
  $('.edit_warning').hide();
  local_id = this.value;
  $("#edit_title").val("");
  $("#edit_desc").val("");
  $("#edit_date").val("");
  $("#edit_from").val("");
  $("#edit_to").val("");

  var d = {
      id: this.value,
      action: "viewOne"
  };
  var dd = {
    "svc" : "calendar",
    "dev" : local_user,
    "msg" : JSON.stringify(d)
  };

  $.ajax({
      url: apiUrl,
      data: JSON.stringify(dd),
      headers: {  'Access-Control-Allow-Origin': '*' },
      error: function(e, jqXHR, textStatus) {
          $('.edit_error').show();
      },
      dataType: 'jsonp',crossDomain: true,
      success: function(data) {
        var data = JSON.parse(JSON.stringify(data));

          if(data["status"] == "Ok")
          {
            $('.edit_error').hide();
            $('.edit_success').hide();
            $('.edit_warning').hide();
            data['msg'] = JSON.parse(JSON.stringify(data['msg']));
            data['msg'] = (JSON.parse(data['msg']));
            var content = "";
            if(data["msg"].length>0)
            {
              $("#edit_title").val(data["msg"][0]["title"]);
              $("#edit_desc").val(data["msg"][0]["cal_desc"]);
              $("#edit_date").val(data["msg"][0]["cal_date"]);
              $("#edit_from").val(data["msg"][0]["time_from"]);
              $("#edit_to").val(data["msg"][0]["time_to"]);
            }
          }
          else if (data["status"] == "Error") {
            $('.edit_error').show();
          }
      },
      type: 'POST'
  });

  // $('#cal-edit-select').hide();
  $('#cal-edit-view').hide();
  $('#cal-edit-edition').show();

});


/* edit */
$("#btnEditBack").click(function() {

  $('#cal-menu').show();
  $('#cal-edit-select').hide();
  $("#edit_select").val("");
  resetFields();

});

$("#btnEditBack2").click(function() {

  $('#cal-edit-select').show();
  $('#cal-edit-view').hide();
  resetFields();

});

$("#btnEditBack3").click(function() {


    var d = {
        user: local_user,
        date: $("#edit_select").val(),
        action: "view"
    };
    var dd = {
      "svc" : "calendar",
      "dev" : local_user,
      "msg" : JSON.stringify(d)
    };

    $.ajax({
        url: apiUrl,
        data: JSON.stringify(dd),
        headers: {  'Access-Control-Allow-Origin': '*' },
        error: function(e, jqXHR, textStatus) {
            $('.edit_error').show();
        },
        dataType: 'jsonp',crossDomain: true,
        success: function(data) {
          var data = JSON.parse(JSON.stringify(data));

            if(data["status"] == "Ok")
            {
              $('.edit_error').hide();
              $('.edit_success').hide();
              $('.edit_warning').hide();
              data['msg'] = JSON.parse(JSON.stringify(data['msg']));
              data['msg'] = (JSON.parse(data['msg']));
              var content = "";
              if(data["msg"].length>0)
              {
                  for(var i=0; i<data["msg"].length; i++){
                    var o = data["msg"][i];
                    var tmp = '<table class="table table-bordered table-sm">' +
                      '<tbody>' +
                        '<tr>' +
                          '<td>'+o.cal_date+'</td>' +
                          '<td>'+o.time_from + '-' + o.time_to+'</td>' +
                        '</tr>' +
                        '<tr>' +
                          '<td colspan="2">'+o.title+'</td>' +
                        '</tr>' +
                        '<tr>' +
                          '<td colspan="2">'+o.cal_desc+'</td>' +
                        '</tr>' +
                        '<tr>' +
                          '<td colspan="2"><button type="button" class="btn btn-secondary btn-block btnEditEntry" value="'+o.id+'">Edit <i class="far fa-edit"></i></button></td>' +
                        '</tr>' +
                      '</tbody>' +
                    '</table>';
                    content = content + tmp;
                  }
                  $("#edit-view-list").html(content);
              }

            }
            else if (data["status"] == "Error") {
              $('.edit_error').show();
            }
        },
        type: 'POST'
    });
  $('#cal-edit-view').show();
  $('#cal-edit-edition').hide();
  resetFields();

});


// edit button
$("#btnEditAction").click(function() {

  $('.edit_error').hide();
  $('.edit_success').hide();
  $('.edit_warning').hide();

    if( !$("#edit_title").val() ||
        !$("#edit_desc").val() ||
        !$("#edit_date").val() ||
        !$("#edit_from").val() ||
        !$("#edit_to").val() ||
        (Date.parse('01/01/2011 ' + $("#edit_from").val() + ":00") > Date.parse('01/01/2011 ' + $("#edit_to").val() + ":00"))
      ){
      $('.edit_warning').show();
      return false;
    }

    var d = {
        user: local_user,
        title: $("#edit_title").val(),
        desc: $("#edit_desc").val(),
        date: $("#edit_date").val(),
        t_from: $("#edit_from").val(),
        t_to: $("#edit_to").val(),
        action: "update",
        id: local_id
    };
    var dd = {
      "svc" : "calendar",
      "dev" : local_user,
      "msg" : JSON.stringify(d)
    };
    logAction("calendar-edit");

    $.ajax({
        url: apiUrl,
        data: JSON.stringify(dd),
        headers: {  'Access-Control-Allow-Origin': '*' },
        error: function(e, jqXHR, textStatus) {
            // console.log('error: ', e);
            // console.log('jqXHR: ', jqXHR);
            // console.log('textStatus: ', textStatus);
            $('.edit_error').show();
        },
        dataType: 'jsonp',crossDomain: true,
        success: function(data) {

            if(data["status"] == "Ok")
            {
                resetFields();
                $('.edit_error').hide();
                $('.edit_success').show();

                setTimeout(function(){
                    $('.edit_success').hide();
                }, 3000);
            }
            else if (data["status"] == "Error") {
              $('.edit_error').show();
            }
        },
        type: 'POST'
    });

});

/***********************************************
                    DELETE
  **********************************************/



/* delete */
$("#btnDelete").click(function() {

  $('#cal-menu').hide();
  $('#cal-delete-select').show();

});
// $("#btnDeleteSubmitDate").click(function() {
//
//   $('#cal-delete-select').hide();
//   $('#cal-delete-deletion').show();
//
// });
//
// /* delete */
// $("#btnDeleteBack").click(function() {
//
//   $('#cal-menu').show();
//   $('#cal-delete-select').hide();
//   resetFields();
//
// });
//
// $("#btnDeleteBack2").click(function() {
//
//   $('#cal-delete-select').show();
//   $('#cal-delete-deletion').hide();
//   resetFields();
//
// });

$("#btnDeleteSubmitDate").click(function() {

  $('.delete_error').hide();
  $('.delete_success').hide();
  $('.delete_warning').hide();

  if( !$("#delete_select").val()){
    $('.delete_warning').show();
    return false;
  }

  var d = {
      user: local_user,
      date: $("#delete_select").val(),
      action: "view"
  };
  var dd = {
    "svc" : "calendar",
    "dev" : local_user,
    "msg" : JSON.stringify(d)
  };
  logAction("calendar-delete");

  $.ajax({
      url: apiUrl,
      data: JSON.stringify(dd),
      headers: {  'Access-Control-Allow-Origin': '*' },
      error: function(e, jqXHR, textStatus) {
          $('.delete_error').show();
      },
      dataType: 'jsonp',crossDomain: true,
      success: function(data) {
        var data = JSON.parse(JSON.stringify(data));

          if(data["status"] == "Ok")
          {
            $('.delete_error').hide();
            $('.delete_success').hide();
            $('.delete_warning').hide();
            data['msg'] = JSON.parse(JSON.stringify(data['msg']));
            data['msg'] = (JSON.parse(data['msg']));
            var content = "";
            if(data["msg"].length>0)
            {
                for(var i=0; i<data["msg"].length; i++){
                  var o = data["msg"][i];
                  var tmp = '<table class="table table-bordered table-sm">' +
                    '<tbody>' +
                      '<tr>' +
                        '<td>'+o.cal_date+'</td>' +
                        '<td>'+o.time_from + '-' + o.time_to+'</td>' +
                      '</tr>' +
                      '<tr>' +
                        '<td colspan="2">'+o.title+'</td>' +
                      '</tr>' +
                      '<tr>' +
                        '<td colspan="2">'+o.cal_desc+'</td>' +
                      '</tr>' +
                      '<tr>' +
                        '<td colspan="2"><button type="button" class="btn btn-danger btn-block btnDeleteEntry" value="'+o.id+'">Delete <i class="fas fa-trash-alt"></i></button></td>' +
                      '</tr>' +
                    '</tbody>' +
                  '</table>';
                  content = content + tmp;
                }
                $("#delete-view-list").html(content);
            }
          }
          else if (data["status"] == "Error") {
            $('.delete_error').show();
          }
      },
      type: 'POST'
  });

  $('#cal-delete-select').hide();
  $('#cal-delete-view').show();
  // $('#cal-delete-deleteion').show();

});

$('#delete-view-list').on('click', '.btnDeleteEntry', function() {

  $('.delete_error').hide();
  $('.delete_success').hide();
  $('.delete_warning').hide();
  local_id = this.value;
  // $("#delete_title").val("");
  // $("#delete_desc").val("");
  // $("#delete_date").val("");
  // $("#delete_from").val("");
  // $("#delete_to").val("");

  var d = {
      id: this.value,
      action: "delete"
  };
  var dd = {
    "svc" : "calendar",
    "dev" : local_user,
    "msg" : JSON.stringify(d)
  };

  $.ajax({
      url: apiUrl,
      data: JSON.stringify(dd),
      headers: {  'Access-Control-Allow-Origin': '*' },
      error: function(e, jqXHR, textStatus) {
          $('.delete_error').show();
      },
      dataType: 'jsonp',crossDomain: true,
      success: function(data) {
        // var data = JSON.parse(JSON.stringify(data));

          if(data["status"] == "Ok")
          {
            $('.delete_error').hide();
            $('.delete_success').hide();
            $('.delete_warning').hide();

            var content = "";
            if(data["status"]==="Ok")
            {
              var d = {
                  user: local_user,
                  date: $("#delete_select").val(),
                  action: "view"
              };
              var dd = {
                "svc" : "calendar",
                "dev" : local_user,
                "msg" : JSON.stringify(d)
              };

              $.ajax({
                  url: apiUrl,
                  data: JSON.stringify(dd),
                  headers: {  'Access-Control-Allow-Origin': '*' },
                  error: function(e, jqXHR, textStatus) {
                      $('.delete_error').show();
                  },
                  dataType: 'jsonp',crossDomain: true,
                  success: function(data) {
                    var data = JSON.parse(JSON.stringify(data));

                      if(data["status"] == "Ok")
                      {
                        $('.delete_error').hide();
                        $('.delete_success').hide();
                        $('.delete_warning').hide();
                        data['msg'] = JSON.parse(JSON.stringify(data['msg']));
                        data['msg'] = (JSON.parse(data['msg']));

                        var content = "";
                        if(data["msg"].length>0)
                        {
                            for(var i=0; i<data["msg"].length; i++){
                              var o = data["msg"][i];
                              var tmp = '<table class="table table-bordered table-sm">' +
                                '<tbody>' +
                                  '<tr>' +
                                    '<td>'+o.cal_date+'</td>' +
                                    '<td>'+o.time_from + '-' + o.time_to+'</td>' +
                                  '</tr>' +
                                  '<tr>' +
                                    '<td colspan="2">'+o.title+'</td>' +
                                  '</tr>' +
                                  '<tr>' +
                                    '<td colspan="2">'+o.cal_desc+'</td>' +
                                  '</tr>' +
                                  '<tr>' +
                                    '<td colspan="2"><button type="button" class="btn btn-danger btn-block btnDeleteEntry" value="'+o.id+'">Delete <i class="fas fa-trash-alt"></i></button></td>' +
                                  '</tr>' +
                                '</tbody>' +
                              '</table>';
                              content = content + tmp;
                            }
                            $("#delete-view-list").html(content);
                        }
                        else {
                          $("#delete-view-list").html("No records");
                        }
                      }
                      else if (data["status"] == "Error") {
                        $('.delete_error').show();
                      }
                  },
                  type: 'POST'
              });
            }
          }
          else if (data["status"] == "Error") {
            $('.delete_error').show();
          }
      },
      type: 'POST'
  });

  // $('#cal-delete-select').hide();
  // $('#cal-delete-view').show();
  // $('#cal-delete-delete').show();

});


$("#btnDeleteBack").click(function() {

  $('#cal-menu').show();
  $('#cal-delete-select').hide();
  $("#delete_select").val("");
  resetFields();

});

$("#btnDeleteBack2").click(function() {

  $('#cal-delete-select').show();
  $('#cal-delete-view').hide();
  resetFields();

});

$("#btnDeleteBack3").click(function() {


    var d = {
        user: local_user,
        date: $("#delete_select").val(),
        action: "view"
    };
    var dd = {
      "svc" : "calendar",
      "dev" : local_user,
      "msg" : JSON.stringify(d)
    };

    $.ajax({
        url: apiUrl,
        data: JSON.stringify(dd),
        headers: {  'Access-Control-Allow-Origin': '*' },
        error: function(e, jqXHR, textStatus) {
            $('.delete_error').show();
        },
        dataType: 'jsonp',crossDomain: true,
        success: function(data) {
          var data = JSON.parse(JSON.stringify(data));

            if(data["status"] == "Ok")
            {
              $('.delete_error').hide();
              $('.delete_success').hide();
              $('.delete_warning').hide();
              data['msg'] = JSON.parse(JSON.stringify(data['msg']));
              data['msg'] = (JSON.parse(data['msg']));

              var content = "";
              if(data["msg"].length>0)
              {
                  for(var i=0; i<data["msg"].length; i++){
                    var o = data["msg"][i];
                    var tmp = '<table class="table table-bordered table-sm">' +
                      '<tbody>' +
                        '<tr>' +
                          '<td>'+o.cal_date+'</td>' +
                          '<td>'+o.time_from + '-' + o.time_to+'</td>' +
                        '</tr>' +
                        '<tr>' +
                          '<td colspan="2">'+o.title+'</td>' +
                        '</tr>' +
                        '<tr>' +
                          '<td colspan="2">'+o.cal_desc+'</td>' +
                        '</tr>' +
                        '<tr>' +
                          '<td colspan="2"><button type="button" class="btn btn-danger btn-block btnDeleteEntry" value="'+o.id+'">Delete <i class="fas fa-trash-alt"></i></button></td>' +
                        '</tr>' +
                      '</tbody>' +
                    '</table>';
                    content = content + tmp;
                  }
                  $("#delete-view-list").html(content);
              }
              else if (data["status"] == "Error") {
                $('.delete_error').show();
              }
            }
            else if (data["status"] == "Error") {
              $('.delete_error').show();
            }
        },
        type: 'POST'
    });
  $('#cal-delete-view').show();
  $('#cal-delete-delete').hide();
  resetFields();

});


$("#btnDeleteAction").click(function() {

  $('.delete_error').hide();
  $('.delete_success').hide();
  $('.delete_warning').hide();

    if( !$("#delete_title").val() ||
        !$("#delete_desc").val() ||
        !$("#delete_date").val() ||
        !$("#delete_from").val() ||
        !$("#delete_to").val() ||
        (Date.parse('01/01/2011 ' + $("#delete_from").val() + ":00") > Date.parse('01/01/2011 ' + $("#delete_to").val() + ":00"))
      ){
      $('.delete_warning').show();
      return false;
    }

    var d = {
        user: local_user,
        title: $("#delete_title").val(),
        desc: $("#delete_desc").val(),
        date: $("#delete_date").val(),
        t_from: $("#delete_from").val(),
        t_to: $("#delete_to").val(),
        action: "update",
        id: local_id
    };
    var dd = {
      "svc" : "calendar",
      "dev" : local_user,
      "msg" : JSON.stringify(d)
    };

    $.ajax({
        url: apiUrl,
        data: JSON.stringify(dd),
        headers: {  'Access-Control-Allow-Origin': '*' },
        error: function(e, jqXHR, textStatus) {
            // console.log('error: ', e);
            // console.log('jqXHR: ', jqXHR);
            // console.log('textStatus: ', textStatus);
            $('.delete_error').show();
        },
        dataType: 'jsonp',crossDomain: true,
        success: function(data) {

            if(data["status"] == "Ok")
            {
                resetFields();
                $('.delete_error').hide();
                $('.delete_success').show();

                setTimeout(function(){
                    $('.delete_success').hide();
                }, 3000);
            }
            else if (data["status"] == "Error") {
              $('.delete_error').show();
            }
        },
        type: 'POST'
    });

});

/***********************************************
                    VIEW
  **********************************************/
  $("#btnView").click(function() {

  $('#cal-menu').hide();
  $('#cal-view-select').show();

});
// $("#btnViewSubmitDate").click(function() {
//
//   $('#cal-view-select').hide();
//   $('#cal-view-list').show();
//
// });
//
// /* view */
// $("#btnViewBack").click(function() {
//
//   $('#cal-menu').show();
//   $('#cal-view-select').hide();
//   resetFields();
//
// });
//
// $("#btnViewBack2").click(function() {
//
//   $('#cal-view-select').show();
//   $('#cal-view-list').hide();
//   resetFields();
//
// });

$("#btnViewSubmitDate").click(function() {

  $('.view_error').hide();
  $('.view_success').hide();
  $('.view_warning').hide();

  if( !$("#view_select").val()){
    $('.view_warning').show();
    return false;
  }

  date_next = $("#view_select").val();
  date_prev = $("#view_select").val();
  date_display = $("#view_select").val();
  $("#date-now").html(date_display);

  var d = {
      user: local_user,
      date: $("#view_select").val(),
      action: "view"
  };
  var dd = {
    "svc" : "calendar",
    "dev" : local_user,
    "msg" : JSON.stringify(d)
  };
  logAction("calendar-view");

  $.ajax({
      url: apiUrl,
      data: JSON.stringify(dd),
      headers: {  'Access-Control-Allow-Origin': '*' },
      error: function(e, jqXHR, textStatus) {
          $('.view_error').show();
      },
      dataType: 'jsonp',crossDomain: true,
      success: function(data) {
          var data = JSON.parse(JSON.stringify(data));
          if(data["status"] == "Ok")
          {
            $('.view_error').hide();
            $('.view_success').hide();
            $('.view_warning').hide();

            data['msg'] = JSON.parse(JSON.stringify(data['msg']));
            data['msg'] = (JSON.parse(data['msg']));

            var content = "";
            if(data["msg"].length>0)
            {
                for(var i=0; i<data["msg"].length; i++){
                  var o = data["msg"][i];
                  var tmp = '<table class="table table-bordered table-sm">' +
                    '<tbody>' +
                      '<tr>' +
                        '<td>'+o.cal_date+'</td>' +
                        '<td>'+o.time_from + '-' + o.time_to+'</td>' +
                      '</tr>' +
                      '<tr>' +
                        '<td colspan="2">'+o.title+'</td>' +
                      '</tr>' +
                      '<tr>' +
                        '<td colspan="2">'+o.cal_desc+'</td>' +
                      '</tr>' +
                      // '<tr>' +
                        // '<td colspan="2"><button type="button" class="btn btn-danger btn-block btnViewEntry" value="'+o.id+'">View <i class="fas fa-trash-alt"></i></button></td>' +
                      // '</tr>' +
                    '</tbody>' +
                  '</table>';
                  content = content + tmp;
                }
                $("#view-view-list").html(content);
            }
          }
          else if (data["status"] == "Error") {
            $('.view_error').show();
          }
      },
      type: 'POST'
  });

  $('#cal-view-select').hide();
  $('#cal-view-view').show();
  // $('#cal-view-viewion').show();

});

$('#view-view-list').on('click', '.btnViewEntry', function() {

  $('.view_error').hide();
  $('.view_success').hide();
  $('.view_warning').hide();
  local_id = this.value;
  // $("#view_title").val("");
  // $("#view_desc").val("");
  // $("#view_date").val("");
  // $("#view_from").val("");
  // $("#view_to").val("");

  var d = {
      id: this.value,
      action: "view"
  };
  var dd = {
    "svc" : "calendar",
    "dev" : local_user,
    "msg" : JSON.stringify(d)
  };

  $.ajax({
      url: apiUrl,
      data: JSON.stringify(dd),
      headers: {  'Access-Control-Allow-Origin': '*' },
      error: function(e, jqXHR, textStatus) {
          $('.view_error').show();
      },
      dataType: 'jsonp',crossDomain: true,
      success: function(data) {

          if(data["status"] == "Ok")
          {
            $('.view_error').hide();
            $('.view_success').hide();
            $('.view_warning').hide();

            var content = "";
            if(data["status"]==="Ok")
            {
              var d = {
                  user: local_user,
                  date: $("#view_select").val(),
                  action: "view"
              };
              var dd = {
                "svc" : "calendar",
                "dev" : local_user,
                "msg" : JSON.stringify(d)
              };

              $.ajax({
                  url: apiUrl,
                  data: JSON.stringify(dd),
                  headers: {  'Access-Control-Allow-Origin': '*' },
                  error: function(e, jqXHR, textStatus) {
                      $('.view_error').show();
                  },
                  dataType: 'jsonp',crossDomain: true,
                  success: function(data) {
                    var data = JSON.parse(JSON.stringify(data));

                      if(data["status"] == "Ok")
                      {
                        $('.view_error').hide();
                        $('.view_success').hide();
                        $('.view_warning').hide();
                        data['msg'] = JSON.parse(JSON.stringify(data['msg']));
                        data['msg'] = (JSON.parse(data['msg']));
                        var content = "";
                        if(data["msg"].length>0)
                        {
                            for(var i=0; i<data["msg"].length; i++){
                              var o = data["msg"][i];
                              var tmp = '<table class="table table-bordered table-sm">' +
                                '<tbody>' +
                                  '<tr>' +
                                    '<td>'+o.cal_date+'</td>' +
                                    '<td>'+o.time_from + '-' + o.time_to+'</td>' +
                                  '</tr>' +
                                  '<tr>' +
                                    '<td colspan="2">'+o.title+'</td>' +
                                  '</tr>' +
                                  '<tr>' +
                                    '<td colspan="2">'+o.cal_desc+'</td>' +
                                  '</tr>' +
                                  // '<tr>' +
                                    // '<td colspan="2"><button type="button" class="btn btn-danger btn-block btnDeleteEntry" value="'+o.id+'">Delete <i class="fas fa-trash-alt"></i></button></td>' +
                                  // '</tr>' +
                                '</tbody>' +
                              '</table>';
                              content = content + tmp;
                            }
                            $("#view-view-list").html(content);
                        }
                      }
                      else if (data["status"] == "Error") {
                        $('.view_error').show();
                      }
                  },
                  type: 'POST'
              });
            }
          }
          else if (data["status"] == "Error") {
            $('.view_error').show();
          }
      },
      type: 'POST'
  });

  // $('#cal-delete-select').hide();
  // $('#cal-delete-view').show();
  // $('#cal-delete-delete').show();

});


$("#btnViewBack").click(function() {

  $('#cal-menu').show();
  $('#cal-view-select').hide();
  $("#view_select").val("");
  resetFields();

});

$("#btnViewBack2").click(function() {

  $('#cal-view-select').show();
  $('#cal-view-view').hide();
  resetFields();

});

$("#btnViewBack3").click(function() {


    var d = {
        user: local_user,
        date: $("#view_select").val(),
        action: "view"
    };
    var dd = {
      "svc" : "calendar",
      "dev" : local_user,
      "msg" : JSON.stringify(d)
    };

    $.ajax({
        url: apiUrl,
        data: JSON.stringify(dd),
        headers: {  'Access-Control-Allow-Origin': '*' },
        error: function(e, jqXHR, textStatus) {
            $('.view_error').show();
        },
        dataType: 'jsonp',crossDomain: true,
        success: function(data) {
          var data = JSON.parse(JSON.stringify(data));

            if(data["status"] == "Ok")
            {
              $('.view_error').hide();
              $('.view_success').hide();
              $('.view_warning').hide();
              data['msg'] = JSON.parse(JSON.stringify(data['msg']));
              data['msg'] = (JSON.parse(data['msg']));
              var content = "";
              if(data["msg"].length>0)
              {
                  for(var i=0; i<data["msg"].length; i++){
                    var o = data["msg"][i];
                    var tmp = '<table class="table table-bordered table-sm">' +
                      '<tbody>' +
                        '<tr>' +
                          '<td>'+o.cal_date+'</td>' +
                          '<td>'+o.time_from + '-' + o.time_to+'</td>' +
                        '</tr>' +
                        '<tr>' +
                          '<td colspan="2">'+o.title+'</td>' +
                        '</tr>' +
                        '<tr>' +
                          '<td colspan="2">'+o.cal_desc+'</td>' +
                        '</tr>' +
                        // '<tr>' +
                          // '<td colspan="2"><button type="button" class="btn btn-danger btn-block btnDeleteEntry" value="'+o.id+'">Delete <i class="fas fa-trash-alt"></i></button></td>' +
                        // '</tr>' +
                      '</tbody>' +
                    '</table>';
                    content = content + tmp;
                  }
                  $("#view-view-list").html(content);
              }
            }
            else if (data["status"] == "Error") {
              $('.view_error').show();
            }
        },
        type: 'POST'
    });
  $('#cal-view-view').show();
  $('#cal-view-view').hide();
  resetFields();

});


$("#view-view-prev").click(function() {

    var newdate = new Date(date_display);
    // console.log(date_display);
    (newdate.setDate(newdate.getDate() - 1));
    var dd = newdate.getDate();
    var mm = newdate.getMonth() + 1;
    var y = newdate.getFullYear();
    // var someFormattedDate = dd + '-' + mm + '-' + y;
    var someFormattedDate = y + '-' + mm + '-' + dd;
    date_display = someFormattedDate;
    date_next = someFormattedDate;
    date_prev = someFormattedDate;
    // console.log(someFormattedDate);
    $("#date-now").html(date_display);

// return false;

    var d = {
        user: local_user,
        date: date_display,
        action: "view",
    };
    var dd = {
      "svc" : "calendar",
      "dev" : local_user,
      "msg" : JSON.stringify(d)
    };

    $.ajax({
        url: apiUrl,
        data: JSON.stringify(dd),
        headers: {  'Access-Control-Allow-Origin': '*' },
        error: function(e, jqXHR, textStatus) {
            $('.view_error').show();
        },
        dataType: 'jsonp',crossDomain: true,
        success: function(data) {
          var data = JSON.parse(JSON.stringify(data));

            if(data["status"] == "Ok")
            {
              $('.view_error').hide();
              $('.view_success').hide();
              $('.view_warning').hide();
              data['msg'] = JSON.parse(JSON.stringify(data['msg']));
              data['msg'] = (JSON.parse(data['msg']));
              var content = "";
              if(data["msg"].length>0)
              {
                  for(var i=0; i<data["msg"].length; i++){
                    var o = data["msg"][i];
                    var tmp = '<table class="table table-bordered table-sm">' +
                      '<tbody>' +
                        '<tr>' +
                          '<td>'+o.cal_date+'</td>' +
                          '<td>'+o.time_from + '-' + o.time_to+'</td>' +
                        '</tr>' +
                        '<tr>' +
                          '<td colspan="2">'+o.title+'</td>' +
                        '</tr>' +
                        '<tr>' +
                          '<td colspan="2">'+o.cal_desc+'</td>' +
                        '</tr>' +
                        // '<tr>' +
                          // '<td colspan="2"><button type="button" class="btn btn-danger btn-block btnDeleteEntry" value="'+o.id+'">Delete <i class="fas fa-trash-alt"></i></button></td>' +
                        // '</tr>' +
                      '</tbody>' +
                    '</table>';
                    content = content + tmp;
                  }
                  $("#view-view-list").html(content);
              }
              else {
                $("#view-view-list").html("No records");
              }
            }else{
              $("#view-view-list").html("No records");
              if (data["status"] == "Error") {
                $('.view_error').show();
              }
            }
        },
        type: 'POST'
    });
  // $('#cal-view-view').show();
  // $('#cal-view-view').hide();
  // resetFields();

});


$("#view-view-next").click(function() {

    var newdate = new Date(date_display);
    // console.log(newdate);
    (newdate.setDate(newdate.getDate() + 1));
    var dd = newdate.getDate();
    var mm = newdate.getMonth() + 1;
    var y = newdate.getFullYear();
    // var someFormattedDate = dd + '-' + mm + '-' + y;
    var someFormattedDate = y + '-' + mm + '-' + dd;
    date_display = someFormattedDate;
    date_next = someFormattedDate;
    date_prev = someFormattedDate;
    // console.log(someFormattedDate);
    $("#date-now").html(date_display);

// return false;

    var d = {
        user: local_user,
        date: date_display,
        action: "view",
    };
    var dd = {
      "svc" : "calendar",
      "dev" : local_user,
      "msg" : JSON.stringify(d)
    };

    $.ajax({
        url: apiUrl,
        data: JSON.stringify(dd),
        headers: {  'Access-Control-Allow-Origin': '*' },
        error: function(e, jqXHR, textStatus) {
            $('.view_error').show();
        },
        dataType: 'jsonp',crossDomain: true,
        success: function(data) {
          var data = JSON.parse(JSON.stringify(data));

            if(data["status"] == "Ok")
            {
              $('.view_error').hide();
              $('.view_success').hide();
              $('.view_warning').hide();
              data['msg'] = JSON.parse(JSON.stringify(data['msg']));
              data['msg'] = (JSON.parse(data['msg']));
              var content = "";
              if(data["msg"].length>0)
              {
                  for(var i=0; i<data["msg"].length; i++){
                    var o = data["msg"][i];
                    var tmp = '<table class="table table-bordered table-sm">' +
                      '<tbody>' +
                        '<tr>' +
                          '<td>'+o.cal_date+'</td>' +
                          '<td>'+o.time_from + '-' + o.time_to+'</td>' +
                        '</tr>' +
                        '<tr>' +
                          '<td colspan="2">'+o.title+'</td>' +
                        '</tr>' +
                        '<tr>' +
                          '<td colspan="2">'+o.cal_desc+'</td>' +
                        '</tr>' +
                        // '<tr>' +
                          // '<td colspan="2"><button type="button" class="btn btn-danger btn-block btnDeleteEntry" value="'+o.id+'">Delete <i class="fas fa-trash-alt"></i></button></td>' +
                        // '</tr>' +
                      '</tbody>' +
                    '</table>';
                    content = content + tmp;
                  }
                  $("#view-view-list").html(content);
              }else {
                $("#view-view-list").html("No records");
              }
            }else{
              $("#view-view-list").html("No records");
              if (data["status"] == "Error") {
                $('.view_error').show();
              }
            }
        },
        type: 'POST'
    });
  // $('#cal-view-view').show();
  // $('#cal-view-view').hide();
  // resetFields();

});

$("#btnViewAction").click(function() {

  $('.view_error').hide();
  $('.view_success').hide();
  $('.view_warning').hide();

    if( !$("#view_title").val() ||
        !$("#view_desc").val() ||
        !$("#view_date").val() ||
        !$("#view_from").val() ||
        !$("#view_to").val() ||
        (Date.parse('01/01/2011 ' + $("#view_from").val() + ":00") > Date.parse('01/01/2011 ' + $("#view_to").val() + ":00"))
      ){
      $('.view_warning').show();
      return false;
    }

    var d = {
        user: local_user,
        title: $("#view_title").val(),
        desc: $("#view_desc").val(),
        date: $("#view_date").val(),
        t_from: $("#view_from").val(),
        t_to: $("#view_to").val(),
        action: "update",
        id: local_id
    };
    var dd = {
      "svc" : "calendar",
      "dev" : local_user,
      "msg" : JSON.stringify(d)
    };

    $.ajax({
        url: apiUrl,
        data: JSON.stringify(dd),
        headers: {  'Access-Control-Allow-Origin': '*' },
        error: function(e, jqXHR, textStatus) {
            // console.log('error: ', e);
            // console.log('jqXHR: ', jqXHR);
            // console.log('textStatus: ', textStatus);
            $('.view_error').show();
        },
        dataType: 'jsonp',crossDomain: true,
        success: function(data) {

            if(data["status"] == "Ok")
            {
                resetFields();
                $('.view_error').hide();
                $('.view_success').show();

                setTimeout(function(){
                    $('.view_success').hide();
                }, 3000);
            }
            else if (data["status"] == "Error") {
              $('.view_error').show();
            }
        },
        type: 'POST'
    });

});

/***********************************************
           Fetch user details
 **********************************************/
function setUser(user_id){
  local_user = user_id;
}

/***********************************************
           AJAX - buttons action
 **********************************************/
function resetFields(){

  $("#add_title").val("");
  $("#add_desc").val("");
  $("#add_date").val("");
  $("#add_from").val("");
  $("#add_to").val("");

  $('#add_error').hide();
  $('#add_success').hide();
  $('#add_warning').hide();

  // $("#view_select").val("");

  $('.view_error').hide();
  $('.view_success').hide();
  $('.view_warning').hide();

  $('.edit_error').hide();
  $('.edit_success').hide();
  $('.edit_warning').hide();

}

function logAction(action){

      var dd = {
        "svc" : "notification",
        "dev" : local_user,
        "msg" : "User: "+local_user+"; Action: "+action
      };

      $.ajax({
          url: apiUrl,
          headers: {  'Access-Control-Allow-Origin': '*' },
          data: JSON.stringify(dd),
          error: function(e, jqXHR, textStatus) {
              // console.log('error: ', e);
              // console.log('jqXHR: ', jqXHR);
              // console.log('textStatus: ', textStatus);
              // $('.edit_error').show();
          },
          dataType: 'jsonp',crossDomain: true,
          success: function(data) {


          },
          type: 'POST'
      });
}
