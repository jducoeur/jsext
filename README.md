# jsext
Commonly-useful extensions for Scala.js, particularly for facade development

In the course of developing quite a lot of complex facades for Querki, I've found some common problems. This is a tiny library to help make it easier to deal with those.

## JSOptionBuilder

At the moment, jsext only contains one utility: JSOptionBuilder. This is designed to deal with a common problem in building facades, especially
facades of JQuery widgets.

A typical such facade is usually initialized with an "options" object that defines its exact behavior. The problem is
that these options objects often contain many fields -- 20-40 fields are not unusual -- some of which are polymorphic in a way that is hard to
represent in Scala. (Since JavaScript is loosely typed, you can have polymorphic unions simply by accepting several different types for a parameter, and
testing what actually got passed in to figure out the type.) The result is a combinatoric explosion, where a single options object could potentially
require hundreds, even thousands of strongly-typed constructors in order to properly represent it.

There are several potential ways to deal with this problem. In JSOptionBuilder, we choose not to even try to define a Scala constructor for the options
object. Instead, we build the options object up through a series of chained function calls. That tames the polymorphism, since each field can be a
separate overloaded function.

### Defining a JSOptionBuilder

JSOptionBuilder is easiest to understand with a worked-out example. In this case, we are going to use jQuery UI's Dialog widget, which has about 30
fields. (You can find the full definition of this Widget [in the jQuery UI API documentation](http://api.jqueryui.com/dialog/). The facade of the widget
itself is quite simple:
```
trait JQueryUIDialogFacade extends js.Object {
  def dialog(options:DialogOptions):JQuery = js.native
}
```
That is, this says that the referenced jQuery object should be considered a Dialog, using the specified DialogOptions. All the work comes in defining
DialogOptions.

To begin with, we provide three related definitions:
```
trait DialogOptions extends js.Object 
object DialogOptions extends DialogOptionBuilder(noOpts)
class DialogOptionBuilder(val dict:OptMap) extends JSOptionBuilder[DialogOptions, DialogOptionBuilder](new DialogOptionBuilder(_)) {
}
```
The `DialogOptions` trait is what we're trying to produce: a facade for the options object that we'll pass into the `dialog()` function above.

The `DialogOptions` *companion object* is an empty DialogOptions. It is legal to pass into `dialog()` (after the implicit conversions we will talk
about later), but is nothing but defaults.

All the interesting stuff goes into the `DialogOptionBuilder` class. Into this, we put a bunch of declarations, one for each overload of each
field, like these:
```
  def appendTo(v:String) = jsOpt("appendTo", v)
  
  def autoOpen(v:Boolean) = jsOpt("autoOpen", v)
  
  def buttons(v:js.Dictionary[js.Function0[Any]]) = jsOpt("buttons", v)
  def buttons(v:js.Array[js.Object]) = jsOpt("buttons", v)
```
That is, for each field, we define a method that takes the type that is legal to pass into that field. We pass that value into jsOpt(),
which returns a new DialogOptionBuilder with that value added -- everything is immutable, and you chain these calls together to get the
fully-constructed options object. If a field accepts multiple types, then you define one overload of the function for each type.

Finally, somewhere near all of this (typically in the package object, but that is not required), you define an implicit that extracts the
results from the built-up options, and converts them to the desired facade trait:
```
implicit def builder2DialogOptions(builder:DialogOptionBuilder) = builder._result
```
That is, this takes a DialogOptionBuilder, and produces a DialogOptions, which you can then pass into the `dialog()` function.

### Using the JSOptionBuilder

Using the resulting class is done with chained function calls instead of a constructor, but the number of characters you actually type
is roughly the same. A typical call looks like this:
```
val asDialog = $(elem).dialog(DialogOptions.
  title(dialogTitle).
  height(height).width(width).
  buttons(buttonMap)
)
```
That is, we start with the `DialogOptions` object defined above (which, remember, is the completely empty default options). We
call the functions for the fields we want to set, chaining them together. The implicit def detects that we are passing this into
`dialog()`, so it converts it from DialogOptionBuilder into DialogOptions.

The resulting code is reasonably concise, and strongly-typed: in good Scala fashion, type errors will be usually be caught in the IDE.

### Summary

If you are building a facade called Foo that takes an options object, you would usually define the following code:
```
trait FooFacade extends js.Object {
  def foo(options:FooOptions):JQuery = js.native
}
trait FooOptions extends js.Object 
object FooOptions extends FooOptionBuilder(noOpts)
class FooOptionBuilder(val dict:OptMap) extends JSOptionBuilder[FooOptions, FooOptionBuilder](new FooOptionBuilder(_)) {
  def field1(v:someType) = jsOpt("field1", v)
  
  def field2(v:someType) = jsOpt("field2", v)
  def field2(v:someOtherType) = jsOpt("field2", v)
  
  // ... one jsOpt for each overload of each field
}
package object foo {
  implicit def builder2FooOptions(builder:FooOptionBuilder) = builder._result
}
```
That's pretty much it. There's a little boilerplate, but not too much, and the resulting facade works well.

Obviously, the details may vary -- use common sense when applying this pattern. But it works as described for a large
number of jQuery widgets.

### TO DO

Some of the boilerplate involved in using JSOptionBuilder could probably be tamed with a few macros.

### License

Copyright (c) 2015 Querki Inc. (justin at querki dot net)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
