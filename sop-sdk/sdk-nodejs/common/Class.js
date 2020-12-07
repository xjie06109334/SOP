/**
 * 面相对象辅助类，可实现类的创建，继承，方法重写
 *
 <pre>
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
 </pre>
 *
 */
exports.Class = (function () {
    // ------Class Creation------
    var initializing = false,
        fnTest = /xyz/.test(function () {
            xyz;
        }) ? /\b_super\b/ : /.*/;

    // The base Class implementation (does nothing)
    this.Class = function () {
    };

    // Create a new Class that inherits from this class
    Class.extend = function (prop) {
        var _super = this.prototype;

        // Instantiate a base class (but only create the instance,
        // don't run the init constructor)
        initializing = true;
        var prototype = new this();
        initializing = false;

        // Copy the properties over onto the new prototype
        for (var name in prop) {
            // Check if we're overwriting an existing function
            prototype[name] = typeof prop[name] == "function" && typeof _super[name] == "function" && fnTest.test(prop[name]) ? (function (name, fn) {
                return function () {
                    var tmp = this._super;

                    // Add a new ._super() method that is the same method
                    // but on the super-class
                    this._super = _super[name];

                    // The method only need to be bound temporarily, so we
                    // remove it when we're done executing
                    var ret = fn.apply(this, arguments);
                    this._super = tmp;

                    return ret;
                };
            })(name, prop[name]) : prop[name];
        }

        // The dummy class constructor
        function Class() {
            // All construction is actually done in the init method
            if (!initializing && this.init) this.init.apply(this, arguments);
        }

        // Populate our constructed prototype object
        Class.prototype = prototype;

        // Enforce the constructor to be what we expect
        Class.prototype.constructor = Class;

        // And make this class extendable
        Class.extend = arguments.callee;

        return Class;
    };// ------Class Creation end------



    return {
        /**
         * 创建一个类
         * @param option 类方法,json数据
         * @param parentClass 父类
         */
        create: function (option, parentClass) {
            if (!parentClass) {
                parentClass = Class;
            }
            return parentClass.extend(option);
        }
    };

})();
