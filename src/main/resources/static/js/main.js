//$.fn.editable.defaults.mode = 'inline';

$(document).ready(function() {
    if($('#manage-task').length > 0) {
        $('#task-navigation').removeClass('active-link');
    }

    $('#task-summary').on('shown', function(e, editable) {
        $('.editable-input').addClass('width-100');
    });

    $('#task-summary').on('hidden', function(e, reason) {
        $('.editable-input').removeClass('width-100');
    });

    $('#task-summary').editable({
        title: 'Add summary',
        escape: true,
        placeholder: 'Add summary',
        mode: 'inline',
        rows: 2,
        ajaxOptions: {
            headers: {
                "X-CSRF-TOKEN": $('.csrf').val()
            }
        },
        success: function(response, newValue) {
            if(response.status != 'error') window.location.reload(); //msg will be shown in editable form
        }
    });

    $('#task-type').editable({
        source: '/types/getForSelect',
        select2: {
            width: 200,
            placeholder: 'Select type'
        },
        ajaxOptions: {
            headers: {
                "X-CSRF-TOKEN": $('.csrf').val()
            }
        },
        success: function(response, newValue) {
            if(response.status != 'error') window.location.reload(); //msg will be shown in editable form
        }
    });

    $('#task-priority').editable({
        source: '/priorities/getForSelect',
        select2: {
            width: 200,
            placeholder: 'Select priority'
        },
        ajaxOptions: {
            headers: {
                "X-CSRF-TOKEN": $('.csrf').val()
            }
        },
        success: function(response, newValue) {
            if(response.status != 'error') window.location.reload(); //msg will be shown in editable form
        }
    });

    $('#task-assignee').editable({
        source: '/accounts/getForSelect',
        select2: {
            width: 200,
            placeholder: 'Select assignee'
        },
        ajaxOptions: {
            headers: {
                "X-CSRF-TOKEN": $('.csrf').val()
            }
        },
        success: function(response, newValue) {
            if(response.status != 'error') window.location.reload(); //msg will be shown in editable form
        }
    });

    $('#task-state').editable({
        source: [
            {id: 'CANCELED', text: 'CANCELED'},
            {id: 'TO DO', text: 'TO DO'},
            {id: 'IN PROGRESS', text: 'IN PROGRESS'},
            {id: 'DONE', text: 'DONE'}
        ],
        select2: {
            width: 200,
            placeholder: 'Select state'
        },
        ajaxOptions: {
            headers: {
                "X-CSRF-TOKEN": $('.csrf').val()
            }
        },
        success: function(response, newValue) {
            if(response.status != 'error') window.location.reload(); //msg will be shown in editable form
        }
    });

    $('#task-resolution').editable({
        source: '/resolutions/getForSelect',
        select2: {
            width: 200,
            placeholder: 'Select resolution'
        },
        ajaxOptions: {
            headers: {
                "X-CSRF-TOKEN": $('.csrf').val()
            }
        },
        success: function(response, newValue) {
            if(response.status != 'error') window.location.reload(); //msg will be shown in editable form
        }
    });

    $('#task-description').on('shown', function(e, editable) {
        $('#description').removeClass('description-border');
        $('.editable-input').addClass('width-100');
    });

    $('#task-description').on('hidden', function(e, reason) {
        $('#description').addClass('description-border');
        $('.editable-input').removeClass('width-100');
    });

    $('#task-description').editable({
        title: 'Add description',
        rows: 10,
        escape: true,
        placeholder: 'Add Description',
        mode: 'inline',
        ajaxOptions: {
            headers: {
                "X-CSRF-TOKEN": $('.csrf').val()
            }
        },
        success: function(response, newValue) {
            if(response.status != 'error') window.location.reload(); //msg will be shown in editable form
        }
    });

    $('#task-summary').removeClass('editable-click');
    $('#task-type').removeClass('editable-click');
    $('#task-priority').removeClass('editable-click');
    $('#task-state').removeClass('editable-click');
    $('#task-assignee').removeClass('editable-click');
    $('#task-resolution').removeClass('editable-click');
    $('#task-description').removeClass('editable-click');
    $('#task-description').removeClass('form-inline');
    $('#task-time-worked').removeClass('editable-click');
});

$('#create-comment-btn').click(function() {
    $('#create-comment-btn').addClass('dn');
    $('#content-container').animate({ scrollTop: $(document).height() }, "fast");
});

$('#cancel-btn').click(function() {
    $('#create-comment-btn').removeClass('dn');
    $('#create-comment').removeClass('in');
    $('#comment-text').val('');
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
        $(this).find("a").addClass(classToAdd);
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

$('#canceledTasks').droppable( {
    drop: function (event, ui) {
        var state = "CANCELED";

        if(ui.draggable.find('.state').val() != state) {
            handleTaskDrop(event, ui, state);
            $(ui.draggable).detach().css({top: 0,left: 0}).appendTo($(this));
        }

        $('#canceledTasks').removeClass('drop-active');
    },
    over: function (event, ui) {
        $('#canceledTasks').addClass('drop-active');
    },

    out: function (event, ui) {
        $('#canceledTasks').removeClass('drop-active');
    }
} );

$('#toDoTasks').droppable( {
    drop: function (event, ui) {
        var state = "TO_DO";

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
            "X-CSRF-TOKEN": $('.csrf').val()
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