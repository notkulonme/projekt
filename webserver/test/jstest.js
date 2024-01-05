class Quiz{
    constructor(question, answ1, answ2, goodAnsw){
        this.question = question;
        this.answ1 = answ1;
        this.answ2 = answ2;
        this.goodAnsw = goodAnsw;
    }
    //this method randomize the answears and returns them as a list, the first element of the list is the question
    shufle(){
        const notRan = [this.answ1,this.answ2,this.goodAnsw];
        let i1 = Math.floor(Math.random() * 3);
        let i2 = Math.floor(Math.random() * 3);
        while(i1 == i2){
            i2 = Math.floor(Math.random() * 3);
        }
        let i3 = 3-i1-i2;
        return [this.question, notRan[i1], notRan[i2], notRan[i3]];

    }
    //this stuff is just for debuging
    print(){
        console.log(this.question, this.answ1,  this.answ2, this.goodAnsw)
    }

}

function loadFile(filePath) {
    var result = null;
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.open("GET", filePath, false);
    xmlhttp.send();
    if (xmlhttp.status==200) {
        result = xmlhttp.responseText;
    }
    return result;
}
function getQuiz(){
    var qzList = [];
    var qzFile = loadFile("quiz.qz");
    qzFile = qzFile.replace(/\r/g, '');
    var allLines = qzFile.split("\n");//with comments
    for(var i = 0; i < allLines.length; i++){
        let line = allLines[i]
        if(!line.includes("#")) {
            let data = line.split(";");
            let quiz = new Quiz(data[0],data[1],data[2],data[3]);
            qzList.push(quiz);
        }
    }
    return qzList;
}
// this array contains all of the questions and answears in a Quiz class
var qzList = getQuiz();
var pontok = 0;


for(var i = 0; i < qzList.length; i++){
    const html = document.createElement("p");
    const arr = qzList[i].shufle();
    html.innerText = arr[0]+"\n"+arr[1]+", "+arr[2]+", "+arr[3];
    document.body.appendChild(html);
}