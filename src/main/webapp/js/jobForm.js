
var jobList = [];
function Add() {
    event.preventDefault();
    function isValidNum( num ) {
        if ( num == 0 ) return true;
        return res = ( num / num ) ? true : false;
    }

    if (devQualification.value && devNum.value && jobDescription.value && isValidNum(devNum.value)) {
        if(devNum.value > 0) {
            jobList.push({
                "jobDescription": jobDescription.value,
                "devQualification": devQualification.value,
                "devNum": devNum.value
            })
            li = drawJobList(jobList.length - 1)
            document.getElementById('jobUl').appendChild(li);
            li = addHidden(jobList.length - 1);
            document.getElementById("hidden_job").appendChild(li);
            devQualification.value = null;
            devNum.value = null;
            jobDescription.value = null;
        }else{
            alert("Amount should me > 0");
        }
    } else {
        alert("Wrong data");
    }
}

function addHidden(i){
    var hiddenData = document.createElement('input');
    hiddenData.setAttribute("type", "hidden");
    hiddenData.setAttribute("name", "job_"+i);
    hiddenData.setAttribute("value", JSON.stringify(jobList[i]));
    return hiddenData;
}
function drawJobList(i) {
    var li = document.createElement('li');
    li.setAttribute('id', i);
    li.innerHTML = jobList[i].jobDescription;
    return li;
}
function Clear() {
    if (jobList.length !== 0) {
        var i;
        var ul = document.getElementById('jobUl');
        for (i = 0; i < jobList.length; i++) {
            ul.removeChild(ul.firstElementChild);
        }
        jobList = [];
    }
}

window.onload = function() {
    var addBtn = document.getElementById('Addbtn');
    var clearBtn = document.getElementById('Clearbtn');

   if(localStorage.jobs != null) {
       jobList = JSON.parse(localStorage.jobs);
   }
    addBtn.addEventListener("click", Add, false);
   // addBtn.addEventListener("click", Hide, false);
    clearBtn.addEventListener("click", Clear, false);
    var addnewbtn = document.getElementById("addnewbtn");
    var ul = document.createElement('ul');
    ul.setAttribute('class', 'list-group');
    ul.setAttribute('id', 'jobUl');

    var i;
    document.getElementById("jobs").insertBefore(ul, clearBtn);

    for (i = 0; i < jobList.length; i++) {
        li = drawJobList(i);
        ul.appendChild(li);
    }
};