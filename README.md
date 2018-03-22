
<img src="https://i.imgur.com/liJuDYo.png" width="400"></img>  
  
lib.Ion is a **CSS pre-processor\*\* being built with Java. It's main goal is to work cross-platform with incredible speed. And thanks to Java's ever progressing benchmark speeds this is becoming a reality. At this point I will start to speak about some other bullshit that will make people want to try this bad boy out. If you reading this and hadn't already noticed, this is only placeholder text.  

## Example of the preprocessor:
[ **Ion** Code ]
```sass
(MyVar): blue;
(MySecondVar): orange;
(anothoVariadsadl): #EEEEEE;

.my_class {
    color: red900;
    background: (MyVar) *;
    color: (anothoVariadsadl) *;
}

(MyNewVariable): orange;

.my_otherclass {
    color: (MyNewVariable);
}

$(MyMixin) {
    color: red;
    background: orange;
}

$(MyOtherMixin) {
    width: 50%;
    height: 72%;
    box-shadow: 10px 10px 5px 0px rgba(0,0,0,0.75);
}

.my_mixinclass {
    $MyMixin;
    color: blue;
    $MyOtherMixin;
}
 ```  
[ **CSS** Output (*automatic minimization*) ]
```css
.my_class{color:#FF8A80;background:blue!important;color:#EEEEEE!important;}.my_otherclass{color:orange;}.my_mixinclass{color:red;background:orange;color:blue;width:50%;height:72%;box-shadow: 10px 10px 5px 0px rgba(0,0,0,0.75);}
```
  
  
Documentation (\[better version here\](www.google.com)):  
 \- Nesting  
 \- Sets (aka mixins)  
 \- Shortcuts  
 \- Variables  
 \- Inheritance  
 \- Events  
 \- Palettes  
  
Check out out website! www.main.com/about  
And remember to fork and contribute if you like what main is about.
