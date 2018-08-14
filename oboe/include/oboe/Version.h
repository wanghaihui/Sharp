//
// Created by ac on 18/8/14.
//

#ifndef OBOE_VERSION_H
#define OBOE_VERSION_H

/**
 * A note on use of preprocessor defines:
 *
 * This is one of the few times when it's suitable to use preprocessor defines rather than constexpr
 * Why? Because C++11 requires a lot of boilerplate code to convert integers into compile-time
 * string literals. The preprocessor, despite it's lack of type checking, is more suited to the task
 *
 * See: https://stackoverflow.com/questions/6713420/c-convert-integer-to-string-at-compile-time/26824971#26824971
 *
 */
#endif //OBOE_VERSION_H
