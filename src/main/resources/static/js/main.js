$(document).ready(function() {
    if ($(document).height() > $(window).height()) {
        // scrollbar
        if($("#taskWrapper").length > 0) {
            $("#container").css({ 'display' : 'block'});
        }
    }
});

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

$('.task').draggable( {
    containment: '#content-container',
    cursor: 'move',
    stack: '.task',
    revert: true
} );

$('#toDoTasks').droppable( {
    drop: function (event, ui) {
        var state = "TODO";

        if(ui.draggable.find('.state').val() != state) {
            handleTaskDrop(event, ui, state);
            $(ui.draggable).detach().css({top: 0,left: 0}).appendTo($(this));
        }

        $('#toDoTasks').removeClass('drop-active');
    },
    over: function (event, ui) {
        $('#toDoTasks').addClass('drop-active');
    },

    out: function (event, ui) {
        $('#toDoTasks').removeClass('drop-active');
    }
} );

$('#inProgressTasks').droppable( {
    drop: function (event, ui) {
        var state = "IN_PROGRESS";

        if(ui.draggable.find('.state').val() != state) {
            handleTaskDrop(event, ui, state);
            $(ui.draggable).detach().css({top: 0,left: 0}).appendTo($(this));
        }

        $('#inProgressTasks').removeClass('drop-active');
    },
    over: function (event, ui) {
        $('#inProgressTasks').addClass('drop-active');
    },

    out: function (event, ui) {
        $('#inProgressTasks').removeClass('drop-active');
    }
} );

$('#doneTasks').droppable( {
    drop: function (event, ui) {
        var state = "DONE";

        if(ui.draggable.find('.state').val() != state) {
            handleTaskDrop(event, ui, state);
            $(ui.draggable).detach().css({top: 0,left: 0}).appendTo($(this));
        }

        $('#doneTasks').removeClass('drop-active');
    },
    over: function (event, ui) {
        $('#doneTasks').addClass('drop-active');
    },

    out: function (event, ui) {
        $('#doneTasks').removeClass('drop-active');
    }
} );

function handleTaskDrop( event, ui, state ) {
    var hasError = false;

    var data = {
        "id": parseInt(ui.draggable.find('.task-id').val()),
        "state": state
    };

    $.ajax({
        type: "POST",
        url: "/tasks/update/state",
        contentType: "application/json",
        data: JSON.stringify(data),
        async: false,
        headers : {
            "X-CSRF-TOKEN": ui.draggable.find('.csrf').val()
        },
        success: function() {
            //ui.draggable.draggable( 'option', 'revert', false );
            //ui.draggable.find('.state').val(state);
            window.location.reload();
        },
        error: function() {
            hasError = true;
        }
    });
}