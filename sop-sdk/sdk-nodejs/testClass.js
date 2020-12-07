/**
 * 演示JS面相对象，包括类的创建，继承，方法重写
 * 运行：node testClass.js
 */

const {Class} = require('./common/Class')

function testClass() {
    //-------------------------
    // JS类的创建,继承
    //-------------------------

    // 例子1:-------------------------
    // 创建一个父类
    var Person = Class.create({
        // 构造函数
        init:function(option){
            this.name = option.name;
        }
        ,getName:function() {
            return this.name;
        }
    });

    // 声明类实例
    var Jim = new Person({name:'Jim'});
    console.log('Jim name:' + Jim.getName())

    //例子2:-------------------------

    // 创建一个类,继承Person类，并重写getName
    var Man = Class.create({
        init:function(option) {
            this._super(option);// 调用父类构造函数
            this.age = option.age;
        }
        // 重写父类方法
        ,getName:function() {
            // 调用父类的getName()
            var name = this._super();
            return '我重写了getName方法：{'+name+'}';
        }
    },Person);

    var man = new Man({name:'Tom',age:22});
    console.log('man name:' + man.getName())

    console.log('Jim instanceof Person: ' + (Jim instanceof Person));
    console.log('man instanceof Person: ' + (man instanceof Person));
}

testClass()
