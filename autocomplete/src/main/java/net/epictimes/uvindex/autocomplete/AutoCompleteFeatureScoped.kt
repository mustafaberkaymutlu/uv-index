package net.epictimes.uvindex.autocomplete


import javax.inject.Scope

@Scope
@kotlin.annotation.Retention
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class AutoCompleteFeatureScoped