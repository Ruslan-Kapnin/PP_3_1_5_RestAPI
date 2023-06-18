const baseURL = "/admin"
const loggedUserURL = "/loggedUser"
const userByIdURL = baseURL + "/user/"
const usersURL = baseURL + "/users"
const rolesURL = baseURL + "/roles"
const newUserURL = baseURL + "/new_user"
const deleteBtn = '<a type=\"button\" class=\"btn btn-danger py-1 deleteButton\" href="" style=\"font-size: small\"'
const editBtn = '<a type=\"button\" class=\"btn btn-info py-1 editButton\" href="" style=\"font-size: small\"'

let loggedUser;
let users;
let roles;

let usersTableBtn;
let newUserBtn;


async function updateTable() {
    let response = await fetch(usersURL)
    users = await response.json()
    $("#tbody").empty()
    users.forEach(function (u) {

        $("#tbody").append("<tr>" +
            "<td>" + u.id + "</td>" +
            "<td>" + u.username + "</td>" +
            "<td>" + u.lastName + "</td>" +
            "<td>" + u.age + "</td>" +
            "<td>" + getRoles(u) + "</td>" +
            "<td>" + editBtn + 'id=' + u.id + ">Edit</a>" + "</td>" +
            "<td>" + deleteBtn + 'id=' + u.id + ">Delete</a>" + "</td>" +
            "</tr>"
        )
    })


    $(".deleteButton").on('click', function (event) {
        event.preventDefault()
        $('#editRole option').prop('selected', false)
        let href = userByIdURL + this.id

        $.get(href, function (user) {
            $('#deleteId').val(user.id)
            $('#deleteName').val(user.username)
            $('#deleteLastName').val(user.lastName)
            $('#deleteAge').val(user.age)
            $('#deleteRole').val(user.roles)

            user.roles.forEach(role => {
                $('#deleteRole option[value=' + role.id + ']').prop('selected', true)
            })
            let deleteForm = $("#deleteForm")
            deleteForm.attr('action', href)
            deleteForm.unbind('submit')
            deleteForm.on('submit', function (event) {
                event.preventDefault()
                onDeleteSubmit(event, href, user)
            })
        })

        $('#deleteModal').modal()
    })

    $(".editButton").on('click', function (event) {
        event.preventDefault()
        $('#editRole option').prop('selected', false)
        let href = userByIdURL + this.id

        $.get(href, function (user) {
            $('#editId').val(user.id)
            $('#editName').val(user.username)
            $('#editLastName').val(user.lastName)
            $('#editAge').val(user.age)
            $('#editPassword').val("")

            user.roles.forEach(role => {
                $('#editRole option[value=' + role.id + ']').prop('selected', true)
            })
            let editForm = $("#editForm")
            editForm.attr('action', href)
            editForm.unbind('submit')
            editForm.on('submit', function (event) {
                event.preventDefault()
                onEditSubmit(event, href, user)
            })
        })

        $('#editModal').modal()
    })
}

async function buildPage() {

    //get logger user
    let response = await fetch(loggedUserURL)
    loggedUser = await response.json()

    response = await fetch(rolesURL)
    roles = await response.json()


    $('document').ready(async function () {
        $('#nav_username').text(loggedUser.username)
        $('#nav_roles').text(getRoles(loggedUser))

        usersTableBtn = $('#usersTableBtn')
        newUserBtn = $('#newUserBtn')
        usersTableBtn.on('click', e => {
            e.preventDefault()
            usersTableOnClick()
        })
        newUserBtn.on('click', e => {
            e.preventDefault()
            newUserOnClick()
        })
        usersTableOnClick()

        roles.forEach(role => {
            $('#editRole').append($("<option></option>").text(role.name).val(role.id))
            $('#deleteRole').append($("<option></option>").text(role.name).val(role.id))
            $('#newRole').append($("<option></option>").text(role.name).val(role.id))
        })

        $("#newUserForm").on('submit', e => onNewUserSubmit(e))

        await updateTable()
    })


}


async function onEditSubmit(event, url, user) {
    const form = $(event.target);
    const json = convertFormToJSON(form);
    const method = json._method
    delete json['_method']
    json.id = user.id


    await fetch(url, {
        method: method,
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(json)
    })

    $('#editModal').modal('hide')
    await updateTable()
}

async function onDeleteSubmit(event, url) {
    const form = $(event.target);
    const json = convertFormToJSON(form);
    const method = json._method
    delete json['_method']

    await fetch(url, {method: method})

    $('#deleteModal').modal('hide')
    await updateTable()
}

async function onNewUserSubmit(event) {
    event.preventDefault()
    const form = $(event.target);
    const json = convertFormToJSON(form);

    await fetch(newUserURL, {
        method: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(json)
    })

    $('#newUserForm').trigger('reset')
    await updateTable()
    usersTableOnClick()
}

function convertFormToJSON(form) {
    const array = $(form).serializeArray(); // Encodes the set of form elements as an array of names and values.
    const json = {};
    json.roles = []
    let role = {"id": 0}
    $.each(array, function () {
        if (this.name === "roles") {
            role.id = this.value
            json.roles.push(Object.assign({}, role))
        } else {
            json[this.name] = this.value || ""
        }

    })
    return json;
}

function getRoles(user) {
    return user.roles.map(role => role['name'])
}

function usersTableOnClick() {
    newUserBtn.removeClass('active')
    usersTableBtn.addClass('active')
    $('#usersTable').show()
    $('#newUser').hide()
}

function newUserOnClick() {
    usersTableBtn.removeClass('active')
    newUserBtn.addClass('active')
    $('#usersTable').hide()
    $('#newUser').show()
}

buildPage()


