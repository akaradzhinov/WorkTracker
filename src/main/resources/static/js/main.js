$('.power').each( function() {
    var classToAdd=null;
    switch($(this).find('div').text()) {
        case '1':
            classToAdd = 'lowest';
            break;
        case '2':
            classToAdd = 'low';
            break;
        case '3':
            classToAdd = 'medium';
            break;
        case '4':
            classToAdd = 'high';
            break;
        case '5':
            classToAdd = 'highest';
            break;
    }

    if( classToAdd !== null ) {
        $(this).find("span").addClass(classToAdd);
    }
} );

/**
 * Click handler for the left navigation - setting an 'active' state on click
 */
$('#mainnav-menu li').click(function () {
    $('#mainnav-menu li.active-link').removeClass('active-link');
    $(this).addClass('active-link');
});

/**
 initialize calendar widget
 */
document.addEventListener("DOMContentLoaded", function(event) {
    $('.calendarField').datetimepicker();
})

$('#toDoTasks div.border-gray').hover(function(){
    $(this).css('background-color', '#ddd');
}, function(){
    $(this).css('background-color', '#ebf2f9');
});

$('#inProgressTasks div.border-gray').hover(function(){
    $(this).css('background-color', '#ddd');
}, function(){
    $(this).css('background-color', '#ebf2f9');
});

$('#doneTasks div.border-gray').hover(function(){
    $(this).css('background-color', '#ddd');
}, function(){
    $(this).css('background-color', '#ebf2f9');
});
